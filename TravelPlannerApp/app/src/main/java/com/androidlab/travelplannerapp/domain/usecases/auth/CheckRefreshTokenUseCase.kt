package com.androidlab.travelplannerapp.domain.usecases.auth

import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import retrofit2.Call
import javax.inject.Inject

class CheckRefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
)  {
    operator fun invoke(token: String): Call<Boolean>? {
        return authRepository.checkRefreshToken(RefreshTokenRequest(token
        ))
    }
}