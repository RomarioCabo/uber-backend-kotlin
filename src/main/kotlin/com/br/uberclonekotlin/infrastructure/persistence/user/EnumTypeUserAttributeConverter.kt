package com.br.uberclonekotlin.infrastructure.persistence.user

import com.br.uberclonekotlin.domain.user.TypeUser
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class EnumTypeUserAttributeConverter : AttributeConverter<TypeUser?, String?> {
    override fun convertToDatabaseColumn(attribute: TypeUser?): String? {
        if (attribute == null) return null
        if (attribute === TypeUser.DRIVER) return "DRIVER"
        if (attribute === TypeUser.PASSENGER) return "PASSENGER"
        throw IllegalArgumentException("$attribute not supported.")
    }

    override fun convertToEntityAttribute(dbData: String?): TypeUser? {
        if (dbData == null) return null
        if (dbData == "DRIVER") return TypeUser.DRIVER
        if (dbData == "PASSENGER") return TypeUser.PASSENGER
        throw IllegalArgumentException("$dbData not supported.")
    }
}