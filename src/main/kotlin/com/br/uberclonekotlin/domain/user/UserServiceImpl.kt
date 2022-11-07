package com.br.uberclonekotlin.domain.user

import com.br.uberclonekotlin.domain.provider.PersistenceProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(@Autowired private val provider: PersistenceProvider) : UserService {

    override fun authenticateUser(authenticate: UserAuthenticate): User? {
        val user: User = provider.findUserByEmail(authenticate.email)
            ?: throw BusinessRuleException("E-mail informado não encotrado!")

        val encoder = BCryptPasswordEncoder()

        if (!encoder.matches(authenticate.password, user.password)) {
            throw BusinessRuleException("Usuário inválido!")
        }

        return user
    }

    override fun saveUser(user: User): User? {
        if (provider.existsEmail(user.email)) {
            throw BusinessRuleException("E-mail ja cadastrado em nossa base de dados!")
        }

        return provider.saveUser(user)
    }
}