package com.br.uberclonekotlin.application.user

import com.br.uberclonekotlin.application.exception.ApiError
import com.br.uberclonekotlin.domain.user.User
import com.br.uberclonekotlin.domain.user.UserAuthenticate
import com.br.uberclonekotlin.domain.user.UserService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(@Autowired private val service: UserService) {

    @PostMapping(
        value = ["/user/authenticate"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "ACCEPTED",
            content = [Content(schema = Schema(implementation = User::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun authenticateUser(@RequestBody authenticate: UserAuthenticate): ResponseEntity<User?> {
        return ResponseEntity.ok().body(service.authenticateUser(authenticate))
    }

    @PostMapping(
        value = ["/user/create"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "ACCEPTED",
            content = [Content(schema = Schema(implementation = User::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    fun saveUser(@RequestBody user: User): ResponseEntity<User?> {
        user.id = null
        return ResponseEntity.created(URI("")).body(service.saveUser(user))
    }
}