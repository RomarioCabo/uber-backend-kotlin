package com.br.uberclonekotlin.domain.taxi_shipping

import com.br.uberclonekotlin.domain.provider.PersistenceProvider
import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistory
import com.br.uberclonekotlin.domain.taxi_shipping_history.TaxiShippingHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TaxiShippingServiceImpl(
    @Autowired private val provider: PersistenceProvider,
    @Autowired private val historyService: TaxiShippingHistoryService,
) : TaxiShippingService {

    override fun saveTaxiShipping(taxiShipping: TaxiShipping?): TaxiShipping? {
        taxiShipping?.id = null
        taxiShipping?.driver = null
        taxiShipping?.createdAt = LocalDateTime.now()

        val taxiShippingPersistence: TaxiShipping? = provider.saveTaxiShipping(taxiShipping)
        historyService.saveTaxiShippingHistory(
            buildTaxiShippingHistory(taxiShippingPersistence?.id!!),
            null
        )

        return taxiShippingPersistence
    }

    override fun getAllUberEligibleRoutes(): List<TaxiShipping?>? {
        return provider.getAllUberEligibleRoutes()
    }

    private fun buildTaxiShippingHistory(idTaxiShipping: UUID): TaxiShippingHistory {
        return TaxiShippingHistory(
            idTaxiShipping = idTaxiShipping,
            statusRoute = StatusRoute.WAITING_ACCEPT_DRIVER,
            eventDate = LocalDateTime.now(),
        )
    }
}