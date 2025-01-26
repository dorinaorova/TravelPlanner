package com.androidlab.travelplannerapp.data.image

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageService {
    @Headers("Accept: application/json")
    @Multipart
    @POST("user/image/upload/{id}/profile")
    fun uploadProfile(@Part image: MultipartBody.Part, @Path("id") id: String) : Call<String>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("user/image/upload/{id}/background")
    fun uploadBackground(@Part image: MultipartBody.Part, @Path("id") id: String) : Call<String>?
}