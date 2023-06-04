package com.tistory.eclipse4j.auth.api.response

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)
