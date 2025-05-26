package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call

interface UserRepository {
    fun getUserById( id: String) : Call<UserInfo>?

    fun updateUserInfo(id: String, userInfo: UserInfo) : Call<UserInfo>?

    fun searchUser(name: String? = null) : Call<List<UserInfo>>?

    fun follow(followInfo: FollowRequest) : Call<UserInfo>?

    fun unfollow(followInfo: FollowRequest) : Call<UserInfo>?

    fun isFollower(followInfo: FollowRequest) : Call<Boolean>?

    fun likeTravel( id: String,travelId: String) : Call<UserInfo>?

    fun isTravelLiked(id: String,travelId: String) : Call<Boolean>?

}