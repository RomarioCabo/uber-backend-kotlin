package com.br.uberclonekotlin.infrastructure.persistence.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {

    @Transactional(readOnly = true)
    @Query(value = "select count(ue.id) > 0 from UserEntity ue where ue.email = :email")
    fun existsEmail(@Param("email") email: String?): Boolean

    @Transactional(readOnly = true)
    @Query(value = "select ue from UserEntity ue where ue.email = :email")
    fun findUserByEmail(@Param("email") email: String?): UserEntity?
}
