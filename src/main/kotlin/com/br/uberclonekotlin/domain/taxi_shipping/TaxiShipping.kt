package com.br.uberclonekotlin.domain.taxi_shipping

import com.br.uberclonekotlin.domain.user.User
import java.time.LocalDateTime
import java.util.*

class TaxiShipping(
    var id: UUID? = null,
    var destination: Destination? = null,
    var driver: User? = null,
    var passenger: User? = null,
    var createdAt: LocalDateTime? = null
) {

    class Destination(
        var street: String? = null,
        var number: String? = null,
        var state: String? = null,
        var city: String? = null,
        var neighborhood: String? = null,
        var postalCode: String? = null,
        var latitude: Double? = null,
        var longitude: Double? = null,
    )
}