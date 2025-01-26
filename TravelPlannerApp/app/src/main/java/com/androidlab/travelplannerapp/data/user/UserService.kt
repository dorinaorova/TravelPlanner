package com.androidlab.travelplannerapp.data.user

import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @Headers("Accept: application/json")
    @GET("user/findById/{id}")
    fun getUserById(@Path("id") id: String) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @PUT("user/{id}")
    fun updateUserInfo(@Path("id") id: String, @Body userInfo: UserInfo) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @GET("user/all")
    fun searchUser(@Query("name") name: String? = null) : Call<List<UserInfo>>?

}