package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping_history

import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class EnumStatusRouteAttributeConverter :
    AttributeConverter<StatusRoute, String> {
    override fun convertToDatabaseColumn(attribute: StatusRoute?): String? {
        if (attribute == null) return null
        if (attribute === StatusRoute.WAITING_ACCEPT_DRIVER) return "WAITING_ACCEPT_DRIVER"
        if (attribute === StatusRoute.DRIVER_ON_WAY) return "DRIVER_ON_WAY"
        if (attribute === StatusRoute.TRAVELING) return "TRAVELING"
        if (attribute === StatusRoute.FINISHED_ROUTE) return "FINISHED_ROUTE"
        if (attribute === StatusRoute.CANCELED_BY_PASSENGER) return "CANCELED_BY_PASSENGER"
        if (attribute === StatusRoute.CANCELED_BY_DRIVER) return "CANCELED_BY_DRIVER"
        throw IllegalArgumentException("$attribute not supported.")
    }

    override fun convertToEntityAttribute(dbData: String?): StatusRoute? {
        if (dbData == null) return null
        if (dbData == "WAITING_ACCEPT_DRIVER") return StatusRoute.WAITING_ACCEPT_DRIVER
        if (dbData == "DRIVER_ON_WAY") return StatusRoute.DRIVER_ON_WAY
        if (dbData == "TRAVELING") return StatusRoute.TRAVELING
        if (dbData == "FINISHED_ROUTE") return StatusRoute.FINISHED_ROUTE
        throw IllegalArgumentException("$dbData not supported.")
    }
}
