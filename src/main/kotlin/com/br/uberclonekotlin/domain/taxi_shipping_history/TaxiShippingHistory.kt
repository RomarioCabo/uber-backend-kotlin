package com.br.uberclonekotlin.domain.taxi_shipping_history

import java.time.LocalDateTime
import java.util.*

class TaxiShippingHistory(
    var id: UUID? = null,
    var idTaxiShipping: UUID? = null,
    var statusRoute: StatusRoute? = null,
    var eventDate: LocalDateTime? = null,
)