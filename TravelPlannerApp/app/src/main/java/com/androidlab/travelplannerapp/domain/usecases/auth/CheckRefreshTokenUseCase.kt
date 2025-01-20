package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.auth.AuthService
import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import retrofit2.Call
import javax.inject.Inject

class CheckRefreshTokenUseCase @Inject constructor(
    private val authService: AuthService
)  {
    operator fun invoke(token: String): Call<Boolean>? {
        return authService.checkRefreshToken(RefreshTokenRequest(token
        ))
    }
}