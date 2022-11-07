package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping

import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class TaxiShippingManager(@Autowired private val repository: TaxiShippingRepository) {
    @Transactional
    fun saveTaxiShipping(taxiShipping: TaxiShipping?): TaxiShipping? {
        return repository.saveAndFlush(TaxiShippingEntity(taxiShipping)).toModel()
    }

    @Transactional
    fun addDriverInTaxiShipping(idDriver: Int?, id: UUID?) {
        val affectedRows: Int = repository.addDriverInTaxiShipping(idDriver!!, id!!)
        if (affectedRows != 1) {
            throw InternalError("Error on update driver (affected $affectedRows rows)")
        }
    }

    fun getAllUberEligibleRoutes(): List<TaxiShipping?>? {
        val taxiShipping: List<TaxiShippingEntity>? = repository.getAllUberEligibleRoutes()
        return if (taxiShipping.isNullOrEmpty()) null else taxiShipping
            .map{ entity -> entity.toModel() }.toList()
    }
}