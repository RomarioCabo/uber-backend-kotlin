package com.br.uberclonekotlin.integration

import com.br.uberclonekotlin.UberCloneKotlinApplication
import com.br.uberclonekotlin.domain.provider.PersistenceProvider
import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.user.TypeUser
import com.br.uberclonekotlin.domain.user.User
import com.br.uberclonekotlin.util.DomainMockUtil
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.*

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = [UberCloneKotlinApplication::class])
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integtest")
class TaxiShippingControllerTest(
    @LocalServerPort private val port: Int = 0,
    @Autowired private val provider: PersistenceProvider,
    @Autowired private val flyway: Flyway,
    @Autowired private val testRestTemplate: TestRestTemplate
) {
    private val URL = "http://localhost:"
    private val CREATE_TAXI_SHIPPING_URN = "/taxi_shipping/create"
    private val GET_ALL_UBER_ELIGIBLE_ROUTES = "/get_all_uber_eligible_routes"

    @BeforeEach
    fun init() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun shouldSaveTaxiShipping() {
        val passenger: User? = insertUser("passenger@gmail.com", TypeUser.PASSENGER)

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val taxiShipping: TaxiShipping = DomainMockUtil.buildTaxiShipping(null, passenger?.id)
        val url: String = URL + port + CREATE_TAXI_SHIPPING_URN

        val response: ResponseEntity<TaxiShipping> = testRestTemplate.postForEntity(
            url,
            HttpEntity(taxiShipping, headers),
            TaxiShipping::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertNotNull(response.body?.id)

        Assertions.assertEquals(
            taxiShipping.destination?.street, response.body?.destination?.street
        )
        Assertions.assertEquals(
            taxiShipping.destination?.number, response.body?.destination?.number
        )
        Assertions.assertEquals(
            taxiShipping.destination?.city, response.body?.destination?.city
        )
        Assertions.assertEquals(
            taxiShipping.destination?.neighborhood, response.body?.destination?.neighborhood
        )
        Assertions.assertEquals(
            taxiShipping.destination?.postalCode, response.body?.destination?.postalCode
        )
        Assertions.assertEquals(
            taxiShipping.destination?.latitude, response.body?.destination?.latitude
        )
        Assertions.assertEquals(
            taxiShipping.destination?.longitude, response.body?.destination?.longitude
        )
        Assertions.assertNull(response.body?.driver)
        Assertions.assertEquals(1, response.body?.passenger?.id)
        Assertions.assertEquals(
            taxiShipping.passenger?.name, response.body?.passenger?.name
        )
        Assertions.assertEquals(
            taxiShipping.passenger?.lastName, response.body?.passenger?.lastName
        )
        Assertions.assertEquals(
            taxiShipping.passenger?.email, response.body?.passenger?.email
        )
        Assertions.assertEquals(
            taxiShipping.passenger?.password, response.body?.passenger?.password
        )
        Assertions.assertEquals(
            taxiShipping.passenger?.typeUser, response.body?.passenger?.typeUser
        )

        val history: TaxiShippingHistory? = getTaxiShippingHistoryById(
            response.body?.id!!, StatusRoute.WAITING_ACCEPT_DRIVER
        )

        Assertions.assertEquals(response.body?.id, history?.idTaxiShipping)
        Assertions.assertEquals(StatusRoute.WAITING_ACCEPT_DRIVER, history?.statusRoute)
        Assertions.assertNotNull(history?.eventDate)
    }

    @Test
    fun shouldReturnAllUberEligibleRoutes() {
        val users: ArrayList<User> = ArrayList()
        val eligibleRoutes: ArrayList<TaxiShipping> = ArrayList()

        for (i in 0..3) {
            users.add(insertUser("passenger$i@gmail.com", TypeUser.PASSENGER)!!)
        }

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val url: String = URL + port + CREATE_TAXI_SHIPPING_URN

        users.forEach { passenger ->
            val response: ResponseEntity<TaxiShipping> = testRestTemplate.postForEntity(
                url, HttpEntity(
                    DomainMockUtil.buildTaxiShipping(null, passenger.id), headers
                ), TaxiShipping::class.java
            )

            eligibleRoutes.add(response.body!!)
        }

        val urlGetRoutes: String = URL + port + GET_ALL_UBER_ELIGIBLE_ROUTES

        val response: ResponseEntity<Array<TaxiShipping>> = testRestTemplate.getForEntity(
            urlGetRoutes, Array<TaxiShipping>::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(4, response.body?.size)

        Assertions.assertEquals(eligibleRoutes[0].id, response.body?.get(0)?.id)
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.street, response.body?.get(0)?.destination?.street
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.number, response.body?.get(0)?.destination?.number
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.state, response.body?.get(0)?.destination?.state
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.city, response.body?.get(0)?.destination?.city
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.neighborhood,
            response.body?.get(0)?.destination?.neighborhood
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.postalCode,
            response.body?.get(0)?.destination?.postalCode
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.latitude, response.body?.get(0)?.destination?.latitude
        )
        Assertions.assertEquals(
            eligibleRoutes[0].destination?.longitude, response.body?.get(0)?.destination?.longitude
        )
        Assertions.assertNull(response.body?.get(0)?.driver)
        Assertions.assertEquals(
            eligibleRoutes[0].passenger?.id, response.body?.get(0)?.passenger?.id
        )
        Assertions.assertNotNull(response.body?.get(0)?.passenger)
        Assertions.assertNotNull(response.body?.get(0)?.createdAt)
    }

    private fun insertUser(email: String, typeUser: TypeUser): User? {
        val user: User = DomainMockUtil.buildUser(email, "mockpassord", typeUser)
        val responseDB: User? = provider.saveUser(user)
        Assertions.assertNotNull(responseDB)
        return responseDB
    }

    private fun getTaxiShippingHistoryById(
        idTaxiShipping: UUID, statusRoute: StatusRoute
    ): TaxiShippingHistory? {
        val history: TaxiShippingHistory? =
            provider.findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute)
        Assertions.assertNotNull(history)
        return history
    }
}