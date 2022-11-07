package com.br.uberclonekotlin.domain.taxi_shipping_history

import com.br.uberclonekotlin.domain.user.BusinessRuleException
import com.fasterxml.jackson.annotation.JsonValue

enum class StatusRoute(@get:JsonValue val typeUserString: String) {
    WAITING_ACCEPT_DRIVER("WAITING_ACCEPT_DRIVER"),
    DRIVER_ON_WAY("DRIVER_ON_WAY"),
    TRAVELING("TRAVELING"),
    FINISHED_ROUTE("FINISHED_ROUTE"),
    CANCELED_BY_PASSENGER("CANCELED_BY_PASSENGER"),
    CANCELED_BY_DRIVER("CANCELED_BY_DRIVER");

    companion object {
        private val values: MutableMap<String, StatusRoute> = HashMap()

        init {
            for (statusRoute in StatusRoute.values()) {
                values[statusRoute.typeUserString] = statusRoute
            }
        }

        fun toEnum(type: String): StatusRoute {
            for (statusRoute in StatusRoute.values()) {
                if (statusRoute.typeUserString == type) {
                    return statusRoute
                }
            }
            throw BusinessRuleException("Status route entered is invalid")
        }

        fun valueOf(statusRoute: StatusRoute): Boolean {
            val enumTypeUser = values[statusRoute.typeUserString]
            return enumTypeUser != null
        }
    }
}