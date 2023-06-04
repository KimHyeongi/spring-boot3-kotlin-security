package com.tistory.eclipse4j.auth.token.entity

import com.tistory.eclipse4j.auth.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name="token")
class TokenEntity(
    token: String?,
    tokenType: TokenType = TokenType.BEARER,
    revoked:Boolean = false,
    expired:Boolean = false,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity? = null
) {

    @Id
    @GeneratedValue
    @Column(name="id")
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    var tokenType: TokenType = tokenType
        private set

    @Column(name = "token", unique = true)
    var token: String? = token
        private set

    @Column(name = "revoked")
    var revoked:Boolean = revoked
        private set

    @Column(name = "expired")
    var expired:Boolean = expired
        private set

    fun logout() {
        expired = true
        revoked = false
    }

    fun revokeUserTokens() {
        expired = true
        revoked = true
    }
}