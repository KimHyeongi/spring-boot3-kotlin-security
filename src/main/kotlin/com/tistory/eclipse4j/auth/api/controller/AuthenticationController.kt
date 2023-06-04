package com.tistory.eclipse4j.auth.api.controller

import com.tistory.eclipse4j.auth.api.request.AuthenticationRequest
import com.tistory.eclipse4j.auth.api.request.RegisterRequest
import com.tistory.eclipse4j.auth.api.response.AuthenticationResponse
import com.tistory.eclipse4j.auth.api.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException


@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    private val service: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(service.register(request))
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(service.authenticate(request))
    }

    @PostMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        service.refreshToken(request, response)
    }
}