package com.br.uberclonekotlin.util

import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.user.TypeUser
import com.br.uberclonekotlin.domain.user.User
import java.time.LocalDateTime
import java.util.*

class DomainMockUtil {

    companion object {
        fun buildUser(email: String?, password: String?, typeUser: TypeUser?): User {
            return User(
                id = null,
                name = "Mock Name",
                lastName = "Mock Last Name",
                email = email,
                password = password,
                typeUser = typeUser
            )
        }

        fun buildTaxiShipping(idDriver: Int?, idPassenger: Int?): TaxiShipping {
            return TaxiShipping(
                destination = buildDestination(),
                driver = if (idDriver != null) User(id = idDriver) else null,
                passenger = if (idPassenger != null) User(id = idPassenger) else null,
                createdAt = LocalDateTime.now(),
            )
        }

        fun buildTaxiShippingHistory(
            idTaxiShipping: UUID?,
            statusRoute: StatusRoute?
        ): TaxiShippingHistory {
            return TaxiShippingHistory(
                idTaxiShipping = idTaxiShipping,
                statusRoute = statusRoute,
                eventDate = LocalDateTime.now(),
            )
        }

        private fun buildDestination(): TaxiShipping.Destination {
            return TaxiShipping.Destination(
                street = "Rua Corronel Perdigão Sobrinho",
                number = "241",
                state = "CE",
                city = "Russas",
                neighborhood = "Centro",
                postalCode = "62900-000",
                latitude = -4.945766092381092,
                longitude = -37.97288549493051,
            )
        }
    }
}