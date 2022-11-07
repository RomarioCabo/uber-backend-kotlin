package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping_history

import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "taxi_shipping_history")
class TaxiShippingHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private var id: UUID? = null,

    @Column(name = "id_taxi_shipping", nullable = false)
    private var idTaxiShipping: UUID? = null,

    @Column(name = "status_route", nullable = false, unique = true)
    @Convert(converter = EnumStatusRouteAttributeConverter::class)
    private var statusRoute: StatusRoute? = null,

    @Column(name = "event_date", nullable = false)
    private var eventDate: LocalDateTime? = null,
) {

    constructor(taxiShippingHistory: TaxiShippingHistory?) : this() {
        id = taxiShippingHistory?.id
        idTaxiShipping = taxiShippingHistory?.idTaxiShipping
        statusRoute = taxiShippingHistory?.statusRoute
        eventDate = taxiShippingHistory?.eventDate
    }

    fun toModel(): TaxiShippingHistory {
        return TaxiShippingHistory(
            id = id,
            idTaxiShipping = idTaxiShipping,
            statusRoute = statusRoute,
            eventDate = eventDate,
        )
    }
}