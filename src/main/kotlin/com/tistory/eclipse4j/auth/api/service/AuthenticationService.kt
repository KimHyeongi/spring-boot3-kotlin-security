package com.tistory.eclipse4j.auth.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tistory.eclipse4j.auth.api.request.AuthenticationRequest
import com.tistory.eclipse4j.auth.api.request.RegisterRequest
import com.tistory.eclipse4j.auth.api.response.AuthenticationResponse
import com.tistory.eclipse4j.auth.jwt.JwtService
import com.tistory.eclipse4j.auth.token.entity.TokenEntity
import com.tistory.eclipse4j.auth.token.entity.TokenRepository
import com.tistory.eclipse4j.auth.token.entity.TokenType
import com.tistory.eclipse4j.auth.user.entity.UserEntity
import com.tistory.eclipse4j.auth.user.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.function.Consumer


@Service
class AuthenticationService(
    private val repository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    @Transactional(readOnly = false)
    fun register(request: RegisterRequest): AuthenticationResponse {
        val userEntity = RegisterRequest.toEntity(request, passwordEncoder)
        val savedUser = repository.save(userEntity)

        val jwtToken = jwtService.generateToken(userEntity)
        val refreshToken = jwtService.generateRefreshToken(userEntity)

        saveUserToken(savedUser, jwtToken)
        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.pwd
            )
        )
        val user = checkNotNull(repository.findByEmail(request.email)) { "사용자 없음" }
        val jwtToken = jwtService.generateToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)
        revokeAllUserTokens(user)
        saveUserToken(user, jwtToken)
        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
    }

    private fun saveUserToken(user: UserEntity, jwtToken: String) {
        val token = TokenEntity(
            token = jwtToken,
            tokenType = TokenType.BEARER,
            revoked = false,
            expired = false,
            user = user)
        tokenRepository.save(token)
    }

    private fun revokeAllUserTokens(user: UserEntity) {
        val validUserTokens = tokenRepository.findAllValidTokenByUser(user.id!!)
        if (validUserTokens.isEmpty()) return
        validUserTokens.forEach(Consumer<TokenEntity> { token: TokenEntity ->
            token.revokeUserTokens()
        })
        tokenRepository.saveAll(validUserTokens)
    }

    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }
        val refreshToken = authHeader.substring(7)
        val userEmail = checkNotNull(jwtService.extractUsername(refreshToken)) { "사용자 없음" }
        val user = checkNotNull(repository.findByEmail(userEmail)) {"사용자 없음"}

        if (jwtService.isTokenValid(refreshToken, user)) {
            val accessToken = jwtService.generateToken(user)
            revokeAllUserTokens(user)
            saveUserToken(user, accessToken)
            val authResponse = AuthenticationResponse(accessToken = accessToken, refreshToken = refreshToken)
            ObjectMapper().writeValue(response.outputStream, authResponse)
        }
    }
}