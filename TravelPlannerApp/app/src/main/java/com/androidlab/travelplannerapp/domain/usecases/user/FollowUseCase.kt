package com.androidlab.travelplannerapp.domain.usecases.user

import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.service.user.UserService
import retrofit2.Call
import javax.inject.Inject

class UnfollowUseCase @Inject constructor(
    private val userService: UserService
) {
    operator fun invoke(followRequest: FollowRequest): Call<UserInfo>? {
        return userService.unfollow(followRequest)
    }
}

class FollowUseCase @Inject constructor(
    private val userService: UserService
) {
    operator fun invoke(followRequest: FollowRequest): Call<UserInfo>? {
        return userService.follow(followRequest)
    }
}

class IsFollowerUseCase @Inject constructor(
    private val userService: UserService
){
    operator fun invoke(followRequest: FollowRequest): Call<Boolean>? {
        return userService.isFollower(followRequest)
    }
}