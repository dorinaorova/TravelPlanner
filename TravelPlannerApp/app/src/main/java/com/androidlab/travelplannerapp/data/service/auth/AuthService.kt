package com.androidlab.travelplannerapp.data.service.auth

import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import com.androidlab.travelplannerapp.data.model.SignUpRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService: AuthRepository {
    @Headers("Accept: application/json")
    @POST("auth/login")
    override fun login(@Body user: LoginRequest) : Call<LoginResponse>?

    @Headers("Accept: application/json")
    @POST("auth/register")
    override fun singUp(@Body user: SignUpRequest) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @POST("auth/refresh-token/check")
    override fun checkRefreshToken(@Body token: RefreshTokenRequest) : Call<Boolean>?
}