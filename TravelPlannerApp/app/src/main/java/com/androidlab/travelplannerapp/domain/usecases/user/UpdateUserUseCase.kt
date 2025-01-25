package com.androidlab.travelplannerapp.domain.usecases.user


import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.user.UserService
import retrofit2.Call
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private  val userService: UserService
) {
    operator fun invoke(id: String, userInfo: UserInfo) : Call<UserInfo>? = userService.updateUserInfo(id, userInfo)
}