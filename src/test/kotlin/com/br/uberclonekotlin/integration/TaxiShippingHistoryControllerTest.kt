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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = [UberCloneKotlinApplication::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integtest")
class TaxiShippingHistoryControllerTest(
    @LocalServerPort private val port: Int = 0,
    @Autowired private val provider: PersistenceProvider,
    @Autowired private val flyway: Flyway,
    @Autowired private val testRestTemplate: TestRestTemplate
) {

    private val URL = "http://localhost:"
    private val CREATE_HISTORY_URN = "/taxi_shipping_history/create"

    private var passenger: User? = null
    private var driver: User? = null
    private var taxiShipping: TaxiShipping? = null

    @BeforeEach
    fun init() {
        flyway.clean()
        flyway.migrate()
        passenger = insertUser("passenger@gmail.com", TypeUser.PASSENGER)
        driver = insertUser("driver@gmail.com", TypeUser.DRIVER)
        taxiShipping = insertTaxiShipping(null, passenger?.id!!)
    }

    @ParameterizedTest
    @CsvSource(
        "DRIVER_ON_WAY",
        "TRAVELING",
        "FINISHED_ROUTE"
    )
    fun shouldSaveHistory(statusRoute: StatusRoute?) {
        val taxiShippingHistory: TaxiShippingHistory =
            DomainMockUtil.buildTaxiShippingHistory(taxiShipping?.id, statusRoute)

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val response: ResponseEntity<TaxiShippingHistory> = testRestTemplate.postForEntity(
            buildUrl(statusRoute), HttpEntity(taxiShippingHistory, headers), TaxiShippingHistory::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertNotNull(response.body?.id)
        Assertions.assertEquals(
            taxiShippingHistory.idTaxiShipping, response.body?.idTaxiShipping
        )
        Assertions.assertEquals(
            taxiShippingHistory.statusRoute, response.body?.statusRoute
        )
        Assertions.assertNotNull(response.body?.eventDate)
    }

    @Test
    fun shouldCancel() {
        val statusRoute: StatusRoute = StatusRoute.CANCELED_BY_PASSENGER

        val taxiShippingHistory: TaxiShippingHistory =
            DomainMockUtil.buildTaxiShippingHistory(taxiShipping?.id, statusRoute)

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val response: ResponseEntity<TaxiShippingHistory> = testRestTemplate.postForEntity(
            buildUrl(statusRoute), HttpEntity(taxiShippingHistory, headers), TaxiShippingHistory::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertNotNull(response.body?.id)
        Assertions.assertEquals(
            taxiShippingHistory.idTaxiShipping, response.body?.idTaxiShipping
        )
        Assertions.assertEquals(
            taxiShippingHistory.statusRoute, response.body?.statusRoute
        )
        Assertions.assertNotNull(response.body?.eventDate)
    }

    private fun insertUser(email: String, typeUser: TypeUser): User? {
        val user: User = DomainMockUtil.buildUser(email, "mockpassord", typeUser)
        val responseDB: User? = provider.saveUser(user)
        Assertions.assertNotNull(responseDB)
        return responseDB
    }

    private fun insertTaxiShipping(idDriver: Int?, idPassenger: Int): TaxiShipping? {
        val taxiShipping: TaxiShipping = DomainMockUtil.buildTaxiShipping(idDriver, idPassenger)
        val responseDB: TaxiShipping? = provider.saveTaxiShipping(taxiShipping)
        Assertions.assertNotNull(responseDB)
        return responseDB
    }

    private fun buildUrl(statusRoute: StatusRoute?): String {
        if (statusRoute == StatusRoute.DRIVER_ON_WAY) {
            return "$URL$port$CREATE_HISTORY_URN?idDriver=${driver?.id}"
        }

        return "$URL$port$CREATE_HISTORY_URN"
    }
}