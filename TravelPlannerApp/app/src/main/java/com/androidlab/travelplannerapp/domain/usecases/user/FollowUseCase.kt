package com.androidlab.travelplannerapp.domain.usecases.user

import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.UserRepository
import retrofit2.Call
import javax.inject.Inject

class UnfollowUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(followRequest: FollowRequest): Call<UserInfo>? {
        return userRepository.unfollow(followRequest)
    }
}

class FollowUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(followRequest: FollowRequest): Call<UserInfo>? {
        return userRepository.follow(followRequest)
    }
}

class IsFollowerUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(followRequest: FollowRequest): Call<Boolean>? {
        return userRepository.isFollower(followRequest)
    }
}