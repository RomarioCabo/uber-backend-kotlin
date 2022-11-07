package com.br.uberclonekotlin.domain.provider

import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.user.User
import java.util.*

interface PersistenceProvider {
    fun saveUser(user: User): User?

    fun existsEmail(email: String?): Boolean

    fun findUserByEmail(email: String?): User?

    fun saveTaxiShipping(taxiShipping: TaxiShipping?): TaxiShipping?

    fun getAllUberEligibleRoutes(): List<TaxiShipping?>?

    fun addDriverInTaxiShipping(idDriver: Int?, id: UUID?)

    fun saveTaxiShippingHistory(taxiShippingHistory: TaxiShippingHistory?): TaxiShippingHistory?

    fun findTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?,
        statusRoute: StatusRoute?
    ): TaxiShippingHistory?

    fun existsTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?,
        statusRoute: StatusRoute?
    ): Boolean

    fun findTaxiShippingHistoryByIdPassenger(idPassenger: Int): TaxiShippingHistory?
}