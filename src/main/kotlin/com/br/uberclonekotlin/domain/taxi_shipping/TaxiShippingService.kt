package com.br.uberclonekotlin.domain.taxi_shipping

interface TaxiShippingService {

    fun saveTaxiShipping(taxiShipping: TaxiShipping?): TaxiShipping?

    fun getAllUberEligibleRoutes(): List<TaxiShipping?>?
}