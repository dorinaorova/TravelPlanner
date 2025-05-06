package com.androidlab.travelplannerapp.domain.usecases.search

import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.service.user.UserService
import retrofit2.Call
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(private val userService: UserService) {
    operator fun invoke(name: String? = null): Call<List<UserInfo>>? {
        return userService.searchUser(name)
    }
}