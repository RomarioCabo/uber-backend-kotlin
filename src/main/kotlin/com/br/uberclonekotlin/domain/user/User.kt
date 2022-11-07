package com.br.uberclonekotlin.domain.user

class User(
    var id: Int? = null,
    var name: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var typeUser: TypeUser? = null
)