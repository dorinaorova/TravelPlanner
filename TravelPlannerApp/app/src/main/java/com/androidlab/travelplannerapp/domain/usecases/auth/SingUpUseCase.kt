package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.model.SignUpRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call
import javax.inject.Inject

class SingUpUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke(userName: String, password: String, email: String, name: String): Call<UserInfo>? {
        return authService.singUp(SignUpRequest(userName, password, email, name))
    }
}