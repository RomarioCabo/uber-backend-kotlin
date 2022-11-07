package com.br.uberclonekotlin.infrastructure.persistence.taxi_shipping

import com.br.uberclonekotlin.domain.taxi_shipping.TaxiShipping
import com.br.uberclonekotlin.infrastructure.persistence.user.UserEntity
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Table(name = "taxi_shipping")
class TaxiShippingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private var id: UUID? = null,

    @Type(type = "jsonb")
    @Column(name = "destination", columnDefinition = "jsonb", nullable = false)
    private var destination: Destination? = null,

    @ManyToOne
    @JoinColumn(name = "id_driver")
    private var driver: UserEntity? = null,

    @ManyToOne
    @JoinColumn(name = "id_passenger", nullable = false)
    private var passenger: UserEntity? = null,

    @Column(name = "created_at", nullable = false)
    private var createdAt: LocalDateTime? = null
) {

    constructor(taxiShipping: TaxiShipping?) : this() {
        id = taxiShipping?.id
        destination = buildDestination(taxiShipping?.destination!!)
        driver = buildDriver(taxiShipping)
        passenger = buildPassanger(taxiShipping)
        createdAt = taxiShipping.createdAt
    }

    class Destination(
        var street: String? = null,
        var number: String? = null,
        var state: String? = null,
        var city: String? = null,
        var neighborhood: String? = null,
        var postalCode: String? = null,
        var latitude: Double? = null,
        var longitude: Double? = null
    ) {
        fun toModel(): TaxiShipping.Destination {
            return TaxiShipping.Destination(
                street = street,
                number = number,
                state = state,
                city = city,
                neighborhood = neighborhood,
                postalCode = postalCode,
                latitude = latitude,
                longitude = longitude,
            )
        }
    }

    fun toModel(): TaxiShipping {
        return TaxiShipping(
            id = id,
            destination = destination?.toModel(),
            driver = driver?.toModel(),
            passenger = passenger?.toModel(),
            createdAt = createdAt
        )
    }

    private fun buildDestination(destination: TaxiShipping.Destination): Destination {
        return Destination(
            street = destination.street,
            number = destination.number,
            state = destination.state,
            city = destination.city,
            neighborhood = destination.neighborhood,
            postalCode = destination.postalCode,
            latitude = destination.latitude,
            longitude = destination.longitude,
        )
    }

    private fun buildDriver(taxiShipping: TaxiShipping): UserEntity? {
        return if (taxiShipping.driver != null) UserEntity(
            id = taxiShipping.driver!!.id,
            name = taxiShipping.driver!!.name,
            lastName = taxiShipping.driver!!.lastName,
            email = taxiShipping.driver!!.email,
            password = taxiShipping.driver!!.password,
            typeUser = taxiShipping.driver!!.typeUser
        ) else null
    }

    private fun buildPassanger(taxiShipping: TaxiShipping): UserEntity? {
        return if (taxiShipping.passenger != null) UserEntity(
            id = taxiShipping.passenger!!.id,
            name = taxiShipping.passenger!!.name,
            lastName = taxiShipping.passenger!!.lastName,
            email = taxiShipping.passenger!!.email,
            password = taxiShipping.passenger!!.password,
            typeUser = taxiShipping.passenger!!.typeUser
        ) else null
    }
}