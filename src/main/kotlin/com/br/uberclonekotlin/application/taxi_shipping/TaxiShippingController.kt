package com.br.uberclonekotlin.application.taxi_shipping

import com.br.uberclonekotlin.application.exception.ApiError
import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShippingService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class TaxiShippingController(@Autowired private val service: TaxiShippingService) {

    @PostMapping(
        value = ["/taxi_shipping/create"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = [Content(schema = Schema(implementation = TaxiShipping::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    fun saveTaxiShipping(@RequestBody taxiShipping: TaxiShipping): ResponseEntity<TaxiShipping?> {
        return ResponseEntity.created(URI("")).body(service.saveTaxiShipping(taxiShipping))
    }

    @GetMapping(
        value = ["/get_all_uber_eligible_routes"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(schema = Schema(implementation = Array<TaxiShipping>::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun getAllUberEligibleRoutes(): ResponseEntity<List<TaxiShipping?>?> {
        return ResponseEntity.ok().body(service.getAllUberEligibleRoutes())
    }
}