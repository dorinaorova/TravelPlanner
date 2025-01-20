package com.androidlab.travelplannerapp.data.model

data class SignUpRequest(
    val userName: String,
    val password: String,
    val email: String,
    val name: String
)