package com.br.uberclonekotlin.domain.taxi_shipping_history


interface TaxiShippingHistoryService {

    fun saveTaxiShippingHistory(
        taxiShippingHistory: TaxiShippingHistory?,
        idDriver: Int?
    ): TaxiShippingHistory?

    fun findTaxiShippingHistoryByIdPassenger(idPassenger: Int): TaxiShippingHistory?
}