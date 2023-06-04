package com.tistory.eclipse4j.auth.token.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional


interface TokenRepository : JpaRepository<TokenEntity, Long> {
    @Query(
        value = """
            select t from TokenEntity t inner join UserEntity u
            on t.user.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
        """
    )
    fun findAllValidTokenByUser(id: Long): List<TokenEntity>

    fun findByToken(token: String): TokenEntity?
}