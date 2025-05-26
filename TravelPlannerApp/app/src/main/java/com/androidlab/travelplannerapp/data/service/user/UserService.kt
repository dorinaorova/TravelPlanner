package com.androidlab.travelplannerapp.data.service.user

import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService: UserRepository {
    @Headers("Accept: application/json")
    @GET("user/findById/{id}")
    override fun getUserById(@Path("id") id: String) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @PUT("user/{id}")
    override fun updateUserInfo(@Path("id") id: String, @Body userInfo: UserInfo) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @GET("user/all")
    override fun searchUser(@Query("name") name: String?) : Call<List<UserInfo>>?

    @Headers("Accept: application/json")
    @PUT("user/follow")
    override fun follow(@Body followInfo: FollowRequest) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @PUT("user/unfollow")
    override fun unfollow(@Body followInfo: FollowRequest) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @PUT("user/is-follower")
    override fun isFollower(@Body followInfo: FollowRequest) : Call<Boolean>?

    @Headers("Accept: application/json")
    @GET("user/travel/like/{id}/{travelId}")
    override fun likeTravel(@Path("id") id: String, @Path("travelId") travelId: String) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @GET("user/travel/liked/{id}/{travelId}")
    override fun isTravelLiked(@Path("id") id: String, @Path("travelId") travelId: String) : Call<Boolean>?
}