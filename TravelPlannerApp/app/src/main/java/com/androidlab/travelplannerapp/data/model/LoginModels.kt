package com.androidlab.travelplannerapp.data.model

data class LoginResponse(
    val id: String,
    val jwt: String,
    val refreshToken: String
)

data class LoginRequest(
    val userName: String,
    val password: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)
