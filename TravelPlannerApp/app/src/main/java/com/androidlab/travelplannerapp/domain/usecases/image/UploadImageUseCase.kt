package com.androidlab.travelplannerapp.domain.usecases.image

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.ImageRepository
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val imageRepository: ImageRepository
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<UserInfo>? {
        return imageRepository.uploadProfile(file, id)
    }
}

class UploadBackgroundUseCase @Inject constructor(
    private val imageRepository: ImageRepository
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<UserInfo>? {
        return imageRepository.uploadBackground(file, id)
    }
}

class UploadTravelImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<Travel>? {
        return imageRepository.uploadTravel(file, id)
    }
}