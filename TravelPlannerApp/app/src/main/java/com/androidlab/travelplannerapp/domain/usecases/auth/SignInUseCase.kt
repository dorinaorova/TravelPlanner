package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.auth.AuthService
import com.androidlab.travelplannerapp.data.model.LoginRequest

class SignInUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(LoginRequest(email, password))
}