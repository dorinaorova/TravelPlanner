package com.androidlab.travelplannerapp.data.service.auth

import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import com.androidlab.travelplannerapp.data.model.SignUpRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Accept: application/json")
    @POST("auth/login")
    fun login(@Body user: LoginRequest) : Call<LoginResponse>?

    @Headers("Accept: application/json")
    @POST("auth/register")
    fun singUp(@Body user: SignUpRequest) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @POST("auth/refresh-token/check")
    fun checkRefreshToken(@Body token: RefreshTokenRequest) : Call<Boolean>?
}