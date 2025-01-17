package com.androidlab.travelplannerapp.data.auth

import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "http://localhost:8080/auth/"

interface AuthService {
    @Headers("Accept: application/json")
    @POST("login")
    abstract fun login(@Body user: LoginRequest) : Call<LoginResponse>?

    companion object {
        var apiService: AuthService? = null
        fun getInstance(): AuthService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(AuthService::class.java)
            }
            return apiService!!
        }
    }
}