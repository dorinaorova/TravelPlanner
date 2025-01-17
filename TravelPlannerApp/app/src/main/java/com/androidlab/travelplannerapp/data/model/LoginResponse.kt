package com.androidlab.travelplannerapp.data.model

data class LoginResponse(
    val id: String,
    val jwt: String
)

data class LoginRequest(
    val username: String,
    val password: String
)
