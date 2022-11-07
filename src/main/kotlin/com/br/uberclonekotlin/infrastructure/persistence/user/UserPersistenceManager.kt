package com.br.uberclonekotlin.infrastructure.persistence.user

import com.br.uberclonekotlin.domain.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserPersistenceManager(@Autowired private val repository: UserRepository) {

    @Transactional
    fun saveUser(user: User): User? {
        return repository.saveAndFlush(UserEntity(user)).toModel()
    }

    fun existsEmail(email: String?): Boolean {
        return repository.existsEmail(email)
    }

    fun findUserByEmail(email: String?): User? {
        val user: UserEntity? = repository.findUserByEmail(email)
        return user?.toModel()
    }
}