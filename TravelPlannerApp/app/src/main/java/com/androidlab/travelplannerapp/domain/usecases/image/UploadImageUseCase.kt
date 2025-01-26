package com.androidlab.travelplannerapp.domain.usecases.image

import com.androidlab.travelplannerapp.data.image.ImageService
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val imageService: ImageService
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<String>? {
        return imageService.uploadProfile(file, id)
    }
}

class UploadBackgroundUseCase @Inject constructor(
    private val imageService: ImageService
){
    operator fun invoke(file: MultipartBody.Part, id: String): Call<String>? {
        return imageService.uploadBackground(file, id)
    }
}