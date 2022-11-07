package com.br.uberclonekotlin.domain.user

import com.fasterxml.jackson.annotation.JsonValue

enum class TypeUser(@get:JsonValue val typeUserString: String) {
    DRIVER("DRIVER"),
    PASSENGER("PASSENGER");

    companion object {
        private val values: MutableMap<String, TypeUser> = HashMap()

        init {
            for (typeUser in TypeUser.values()) {
                values[typeUser.typeUserString] = typeUser
            }
        }

        fun toEnum(type: String): TypeUser {
            for (typeUser in TypeUser.values()) {
                if (typeUser.typeUserString == type) {
                    return typeUser
                }
            }
            throw BusinessRuleException("Type of user entered is invalid")
        }

        fun valueOf(type: TypeUser): Boolean {
            val enumTypeUser = values[type.typeUserString]
            return enumTypeUser != null
        }
    }
}