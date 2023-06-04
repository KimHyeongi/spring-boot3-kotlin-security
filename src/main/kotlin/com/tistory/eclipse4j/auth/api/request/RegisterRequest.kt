package com.tistory.eclipse4j.auth.api.request

import com.tistory.eclipse4j.auth.user.entity.Role
import com.tistory.eclipse4j.auth.user.entity.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder

data class RegisterRequest (
    val firstname: String,
    val lastname: String,
    val email: String,
    val pwd: String,
    val role: Role,
) {

    companion object {
        fun toEntity(request: RegisterRequest, passwordEncoder: PasswordEncoder): UserEntity {
            return UserEntity(
                firstname = request.firstname,
                lastname = request.lastname,
                email = request.email,
                pwd = passwordEncoder.encode(request.pwd),
                role = request.role
            )
        }
    }

}