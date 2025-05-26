package com.androidlab.travelplannerapp.domain.usecases.user


import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.UserRepository
import retrofit2.Call
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private  val userRepository: UserRepository
) {
    operator fun invoke(id: String, userInfo: UserInfo) : Call<UserInfo>? = userRepository.updateUserInfo(id, userInfo)
}

class LikeTravelUseCase @Inject constructor(
    private  val userRepository: UserRepository
) {
    operator fun invoke(id: String, travelId: String) : Call<UserInfo>? = userRepository.likeTravel(id, travelId)
}

class IsTravelLikedUseCase @Inject constructor(
    private  val userRepository: UserRepository
) {
    operator fun invoke(id: String, travelId: String) : Call<Boolean>? = userRepository.isTravelLiked(id, travelId)
}