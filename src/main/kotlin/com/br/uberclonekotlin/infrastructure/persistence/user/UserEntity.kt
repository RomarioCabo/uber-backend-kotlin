package com.br.uberclonekotlin.infrastructure.persistence.user

import com.br.uberclonekotlin.domain.user.TypeUser
import com.br.uberclonekotlin.domain.user.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

@Entity
@Table(name = "user_app", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("email"))])
class UserEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Int? = null,

    @Column(name = "name", nullable = false)
    private var name: String? = null,

    @Column(name = "last_name", nullable = false)
    private var lastName: String? = null,

    @Column(name = "email", nullable = false)
    private var email: String? = null,

    @Column(name = "password", nullable = false)
    private var password: String? = null,

    @Column(name = "type", nullable = false)
    @Convert(converter = EnumTypeUserAttributeConverter::class)
    private var typeUser: TypeUser? = null,
) {

    constructor(user: User) : this() {
        id = user.id
        name = user.name
        lastName = user.lastName
        email = user.email
        password = encoderPassword(user.password)
        typeUser = user.typeUser
    }

    fun toModel(): User {
        return User(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            password = password,
            typeUser = typeUser
        )
    }

    private fun encoderPassword(password: String?): String {
        val encoder = BCryptPasswordEncoder()
        return encoder.encode(password)
    }
}