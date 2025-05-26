package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import retrofit2.Call
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Call<LoginResponse>? {
        return authRepository.login(LoginRequest(email, password))
    }
}