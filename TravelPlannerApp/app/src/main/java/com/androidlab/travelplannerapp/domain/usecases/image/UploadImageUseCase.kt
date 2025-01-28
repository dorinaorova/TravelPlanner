package com.androidlab.travelplannerapp.domain.usecases.image

import com.androidlab.travelplannerapp.data.image.ImageService
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val imageService: ImageService
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<UserInfo>? {
        return imageService.uploadProfile(file, id)
    }
}

class UploadBackgroundUseCase @Inject constructor(
    private val imageService: ImageService
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<UserInfo>? {
        return imageService.uploadBackground(file, id)
    }
}

class UploadTravelImageUseCase @Inject constructor(
    private val imageService: ImageService
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<Travel>? {
        return imageService.uploadTravel(file, id)
    }
}