package com.androidlab.travelplannerapp.domain.usecases.user


import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.service.user.UserService
import retrofit2.Call
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private  val userService: UserService
) {
    operator fun invoke(id: String, userInfo: UserInfo) : Call<UserInfo>? = userService.updateUserInfo(id, userInfo)
}

class LikeTravelUseCase @Inject constructor(
    private  val userService: UserService
) {
    operator fun invoke(id: String, travelId: String) : Call<UserInfo>? = userService.likeTravel(id, travelId)
}

class IsTravelLikedUseCase @Inject constructor(
    private  val userService: UserService
) {
    operator fun invoke(id: String, travelId: String) : Call<Boolean>? = userService.isTravelLiked(id, travelId)
}