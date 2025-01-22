package com.androidlab.travelplannerapp.data.user

import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {
    @Headers("Accept: application/json")
    @GET("user/findById/{id}")
    fun getUserById(@Path("id") id: String) : Call<UserInfo>?
}