package com.tistory.eclipse4j.auth.user.repository

import com.tistory.eclipse4j.auth.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<UserEntity, Int?> {
    fun findByEmail(email: String): UserEntity?
}