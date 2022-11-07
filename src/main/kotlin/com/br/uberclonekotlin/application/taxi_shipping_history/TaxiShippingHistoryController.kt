package com.br.uberclonekotlin.application.taxi_shipping_history

import com.br.uberclonekotlin.application.exception.ApiError
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistoryService
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
import java.util.*

@RestController
class TaxiShippingHistoryController(@Autowired private val service: TaxiShippingHistoryService) {

    @PostMapping(
        value = ["/taxi_shipping_history/create"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = [Content(schema = Schema(implementation = TaxiShippingHistory::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    fun saveTaxiShippingHistory(
        @RequestBody history: TaxiShippingHistory,
        @RequestParam(required = false) idDriver: Int?
    ): ResponseEntity<TaxiShippingHistory?>? {
        return ResponseEntity.created(URI(""))
            .body(service.saveTaxiShippingHistory(history, idDriver))
    }

    @GetMapping(
        value = ["/taxi_shipping_history/passenger/waiting_accept_driver/{idPassenger}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(schema = Schema(implementation = TaxiShippingHistory::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        )
    )
    @ResponseStatus(value = HttpStatus.OK)
    fun findTaxiShippingHistoryByIdPassenger(@PathVariable idPassenger: Int): ResponseEntity<TaxiShippingHistory?>? {
        return ResponseEntity.ok().body(service.findTaxiShippingHistoryByIdPassenger(idPassenger))
    }
}