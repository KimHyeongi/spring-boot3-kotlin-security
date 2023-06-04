package com.tistory.eclipse4j.auth.token.service

import com.tistory.eclipse4j.auth.token.entity.TokenEntity
import com.tistory.eclipse4j.auth.token.entity.TokenRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class LogoutService(
    private val tokenRepository: TokenRepository
): LogoutHandler {

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }
        val jwt: String = authHeader.substring(7)
        val storedToken: TokenEntity? = tokenRepository.findByToken(jwt)
        storedToken?.let {
            storedToken.logout()
            tokenRepository.save<TokenEntity>(storedToken)
            SecurityContextHolder.clearContext()
        }
    }
}