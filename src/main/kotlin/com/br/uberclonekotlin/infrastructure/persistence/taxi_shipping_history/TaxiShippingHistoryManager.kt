package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping_history

import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class TaxiShippingHistoryManager(@Autowired private val repository: TaxiShippingHistoryRepository) {

    @Transactional
    fun saveTaxiShippingHistory(taxiShippingHistory: TaxiShippingHistory?): TaxiShippingHistory? {
        return repository.saveAndFlush(TaxiShippingHistoryEntity(taxiShippingHistory)).toModel()
    }

    fun findTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?,
        statusRoute: StatusRoute?
    ): TaxiShippingHistory? {
        return repository
            .findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute)?.toModel()
    }

    fun existsTaxiShippingHistoryByIdTaxiShipping(
        idTaxiShipping: UUID?,
        statusRoute: StatusRoute?
    ): Boolean {
        val taxiShippingHistoryEntity: TaxiShippingHistoryEntity? = repository
            .findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute)
        return taxiShippingHistoryEntity != null
    }

    fun findTaxiShippingHistoryByIdPassenger(idPassenger: Int): TaxiShippingHistory? {
        return repository.findTaxiShippingHistoryByIdPassenger(idPassenger)?.toModel()
    }
}