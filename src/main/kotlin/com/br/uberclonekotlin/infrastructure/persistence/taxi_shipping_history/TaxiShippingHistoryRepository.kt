package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping_history

import com.br.uberclonekotlin.domain.taxi_shipping_history.StatusRoute
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface TaxiShippingHistoryRepository : JpaRepository<TaxiShippingHistoryEntity?, UUID?> {
    @Transactional(readOnly = true)
    @Query(
        value = "select tsh from TaxiShippingHistoryEntity tsh where tsh.idTaxiShipping = :idTaxiShipping " +
                "and tsh.statusRoute = :statusRoute"
    )
    fun findTaxiShippingHistoryByIdTaxiShipping(
        @Param("idTaxiShipping") idTaxiShipping: UUID?,
        @Param("statusRoute") statusRoute: StatusRoute?
    ): TaxiShippingHistoryEntity?

    @Transactional(readOnly = true)
    @Query(
        value = """
            select tsh
                from TaxiShippingHistoryEntity tsh
                         inner join TaxiShippingEntity ts on ts.id = tsh.idTaxiShipping
                where ts.passenger.id = :idPassenger
                  and tsh.statusRoute = 'WAITING_ACCEPT_DRIVER'
                  and not exists(select 1
                                 from TaxiShippingHistoryEntity tsh2
                                 where tsh.idTaxiShipping = tsh2.idTaxiShipping
                                   and tsh2.statusRoute in ('FINISHED_ROUTE', 'CANCELED_BY_PASSENGER', 'CANCELED_BY_DRIVER'))
                   """
    )
    fun findTaxiShippingHistoryByIdPassenger(@Param("idPassenger") idPassenger: Int): TaxiShippingHistoryEntity?
}