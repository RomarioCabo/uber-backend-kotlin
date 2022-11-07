package com.br.uberclonekotlin.domain.user

interface UserService {

    fun authenticateUser(authenticate: UserAuthenticate): User?

    fun saveUser(user: User): User?
}