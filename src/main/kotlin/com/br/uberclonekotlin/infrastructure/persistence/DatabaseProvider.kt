package com.br.uberclonekotlin.infrastructure.persistence

import com.br.uberclonekotlin.domain.provider.PersistenceProvider
import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.user.User
import com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping.TaxiShippingManager
import com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping_history.TaxiShippingHistoryManager
import com.br.uberclonekotlin.infrastructure.persistence.user.UserPersistenceManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class DatabaseProvider(
    @Autowired private val userPersistenceManager: UserPersistenceManager,
    @Autowired private val taxiShippingManager: TaxiShippingManager,
    @Autowired private val taxiShippingHistoryManager: TaxiShippingHistoryManager
) : PersistenceProvider {

    override fun saveUser(user: User): User? {
        return userPersistenceManager.saveUser(user)
    }

    override fun existsEmail(email: String?): Boolean {
        return userPersistenceManager.existsEmail(email)
    }

    override fun findUserByEmail(email: String?): User? {
        return userPersistenceManager.findUserByEmail(email)
    }

    override fun saveTaxiShipping(taxiShipping: TaxiShipping?): TaxiShipping? {
        return taxiShippingManager.saveTaxiShipping(taxiShipping)
    }

    override fun getAllUberEligibleRoutes(): List<TaxiShipping?>? {
        return taxiShippingManager.getAllUberEligibleRoutes()
    }

    override fun addDriverInTaxiShipping(idDriver: Int?, id: UUID?) {
        taxiShippingManager.addDriverInTaxiShipping(idDriver, id)
    }

    override fun saveTaxiShippingHistory(taxiShippingHistory: TaxiShippingHistory?): TaxiShippingHistory? {
        return taxiShippingHistoryManager.saveTaxiShippingHistory(taxiShippingHistory)
    }

    override fun findTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?, statusRoute: StatusRoute?
    ): TaxiShippingHistory? {
        return taxiShippingHistoryManager.findTaxiShippingHistoryByIdTaxiShipping(
            idTaxiShipping, statusRoute
        )
    }

    override fun existsTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?, statusRoute: StatusRoute?
    ): Boolean {
        return taxiShippingHistoryManager.existsTaxiShippingHistoryByIdTaxiShipping(
            idTaxiShipping, statusRoute
        )
    }

    override fun findTaxiShippingHistoryByIdPassenger(idPassenger: Int): TaxiShippingHistory? {
        return taxiShippingHistoryManager.findTaxiShippingHistoryByIdPassenger(idPassenger)
    }
}