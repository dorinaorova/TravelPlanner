package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import retrofit2.Call
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke(email: String, password: String): Call<LoginResponse>? {
        return authService.login(LoginRequest(email, password))
    }
}