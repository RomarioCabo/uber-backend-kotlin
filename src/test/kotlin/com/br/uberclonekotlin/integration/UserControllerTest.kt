package com.br.uberclonekotlin.integration

import com.br.uberclonekotlin.UberCloneKotlinApplication
import com.br.uberclonekotlin.application.exception.ApiError
import com.br.uberclonekotlin.domain.provider.PersistenceProvider
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = [UberCloneKotlinApplication::class])
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integtest")
class UserControllerTest(
    @LocalServerPort private val port: Int = 0,
    @Autowired private val provider: PersistenceProvider,
    @Autowired private val flyway: Flyway,
    @Autowired private val testRestTemplate: TestRestTemplate
) {

    private val URL = "http://localhost:"

    private val CREATE_USER_URN = "/user/create"
    private val AUTHENTICATE_USER_URN = "/user/authenticate"

    @BeforeEach
    fun init() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun shouldThrowExceptionWhenEmailAlreadyExists() {
        insertUser()

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER)
        val url: String = URL + port + CREATE_USER_URN

        val response: ResponseEntity<ApiError> = testRestTemplate
            .postForEntity(url, HttpEntity(user, headers), ApiError::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        Assertions.assertEquals(
            "E-mail ja cadastrado em nossa base de dados!",
            response.body?.reasons?.get(0)
        )
    }

    @Test
    fun shouldSaveUser() {
        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val user: User = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER)

        val url = URL + port + CREATE_USER_URN
        val response: ResponseEntity<User> = testRestTemplate.postForEntity(
            url, HttpEntity(user, headers),
            User::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)

        Assertions.assertNotNull(response.body)

        Assertions.assertEquals(1, response.body?.id)
        Assertions.assertEquals(user.name, response.body?.name)
        Assertions.assertEquals(user.lastName, response.body?.lastName)
        Assertions.assertEquals(user.typeUser, response.body?.typeUser)
    }

    @Test
    fun shouldThrowExceptionWhenAuthenticateAndEmailNotExists() {
        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val user: User = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER)

        val url = URL + port + AUTHENTICATE_USER_URN
        val response: ResponseEntity<ApiError> = testRestTemplate
            .postForEntity(url, HttpEntity(user, headers), ApiError::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals(
            "E-mail informado não encotrado!",
            response.body?.reasons?.get(0)
        )
    }

    @Test
    fun shouldThrowExceptionWhenAuthenticateAndPasswordNotMatches() {
        insertUser()

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val user: User = DomainMockUtil.buildUser("mock@mock.com", "mockpassord3", TypeUser.DRIVER)
        val url = URL + port + AUTHENTICATE_USER_URN
        val response: ResponseEntity<ApiError> = testRestTemplate
            .postForEntity(url, HttpEntity(user, headers), ApiError::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals(
            "Usuário inválido!",
            response.body?.reasons?.get(0)
        )
    }

    @Test
    fun shouldAuthenticateUser() {
        insertUser()

        val headers = HttpHeaders()
        headers["Content-Type"] = "application/json;charset=UTF-8"

        val user: User = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER)
        val url = URL + port + AUTHENTICATE_USER_URN
        val response: ResponseEntity<User> = testRestTemplate
            .postForEntity(url, HttpEntity(user, headers), User::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)

        Assertions.assertNotNull(response.body)

        Assertions.assertEquals(1, response.body?.id)
        Assertions.assertEquals(user.name, response.body?.name)
        Assertions.assertEquals(user.lastName, response.body?.lastName)
        Assertions.assertEquals(user.typeUser, response.body?.typeUser)
        Assertions.assertNotEquals(user.password, response.body?.password)

        Assertions.assertNotNull(response.body?.password)
    }

    private fun insertUser() {
        val user: User = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER)
        val responseDB: User? = provider.saveUser(user)

        Assertions.assertNotNull(responseDB)
    }
}