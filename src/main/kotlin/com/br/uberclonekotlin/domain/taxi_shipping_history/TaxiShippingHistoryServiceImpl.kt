package com.br.uberclonekotlin.domain.taxi_shipping_history

import com.br.uberclonekotlin.domain.provider.PersistenceProvider
import com.br.uberclonekotlin.domain.user.BusinessRuleException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TaxiShippingHistoryServiceImpl(@Autowired private val provider: PersistenceProvider) :
    TaxiShippingHistoryService {

    override fun saveTaxiShippingHistory(
        taxiShippingHistory: TaxiShippingHistory?,
        idDriver: Int?
    ): TaxiShippingHistory? {
        taxiShippingHistory?.id = null
        taxiShippingHistory?.eventDate = LocalDateTime.now()

        val history: TaxiShippingHistory? = provider.findTaxiShippingHistoryByIdTaxiShipping(
            taxiShippingHistory?.idTaxiShipping,
            taxiShippingHistory?.statusRoute
        )

        if (history != null) {
            return history
        }

        addDriverInTaxiShipping(taxiShippingHistory!!, idDriver)

        return provider.saveTaxiShippingHistory(taxiShippingHistory)
    }

    override fun findTaxiShippingHistoryByIdPassenger(idPassenger: Int): TaxiShippingHistory? {
        return provider.findTaxiShippingHistoryByIdPassenger(idPassenger)
    }

    private fun addDriverInTaxiShipping(taxiShippingHistory: TaxiShippingHistory, idDriver: Int?) {
        if (taxiShippingHistory.statusRoute !== StatusRoute.DRIVER_ON_WAY) {
            return
        }

        if (idDriver == null) {
            throw BusinessRuleException("Para o estágio DRIVER_ON_WAY o ID do motorista é obrigatório")
        }

        provider.addDriverInTaxiShipping(idDriver, taxiShippingHistory.idTaxiShipping)

    }
}