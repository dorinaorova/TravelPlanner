package com.androidlab.travelplannerapp.data.service.image

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.data.repository.ImageRepository
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageService : ImageRepository {
    @Headers("Accept: application/json")
    @Multipart
    @POST("user/image/upload/{id}/profile")
    override fun uploadProfile(@Part image: MultipartBody.Part, @Path("id") id: String) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("user/image/upload/{id}/background")
    override fun uploadBackground(@Part image: MultipartBody.Part, @Path("id") id: String) : Call<UserInfo>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("travel/image/upload/{id}")
    override fun uploadTravel(@Part image: MultipartBody.Part, @Path("id") id: String) : Call<Travel>?
}