package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface TaxiShippingRepository : JpaRepository<TaxiShippingEntity, UUID> {

    @Modifying
    @Query(
        value = "UPDATE taxi_shipping SET id_driver = :idDriver WHERE id = :id",
        nativeQuery = true
    )
    fun addDriverInTaxiShipping(@Param("idDriver") idDriver: Int, @Param("id") id: UUID): Int

    @Transactional(readOnly = true)
    @Query(
        value = """
            select ts
            from TaxiShippingEntity ts
                  inner join TaxiShippingHistoryEntity tsh on ts.id = tsh.idTaxiShipping
            where not exists(select 1
                      from TaxiShippingHistoryEntity tsh2
                      where tsh.idTaxiShipping = tsh2.idTaxiShipping
                      and tsh2.statusRoute in ('DRIVER_ON_WAY', 'TRAVELING', 'FINISHED_ROUTE', 'CANCELED_BY_PASSENGER', 'CANCELED_BY_DRIVER'))
        """
    )
    fun getAllUberEligibleRoutes(): List<TaxiShippingEntity>?
}