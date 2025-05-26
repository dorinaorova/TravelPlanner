package com.androidlab.travelplannerapp.domain.usecases.user

import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.UserRepository
import retrofit2.Call
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(id: String): Call<UserInfo>? {
        return userRepository.getUserById(id)
    }
}