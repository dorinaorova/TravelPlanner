package com.androidlab.travelplannerapp.domain.usecases.user

import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.service.user.UserService
import retrofit2.Call
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userService: UserService
) {
    operator fun invoke(id: String): Call<UserInfo>? {
        return userService.getUserById(id)
    }
}