package com.dipterv.dipterv.model.requestModel

data class RegisterRequest(
    val userName: String,
    val password: String,
    val email: String,
    val name: String,
)
