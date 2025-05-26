package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.model.SignUpRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import retrofit2.Call
import javax.inject.Inject

class SingUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(userName: String, password: String, email: String, name: String): Call<UserInfo>? {
        return authRepository.singUp(SignUpRequest(userName, password, email, name))
    }
}