package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import okhttp3.MultipartBody
import retrofit2.Call

interface ImageRepository {

    fun uploadProfile(image: MultipartBody.Part,id: String) : Call<UserInfo>?

    fun uploadBackground(image: MultipartBody.Part,id: String) : Call<UserInfo>?

    fun uploadTravel(image: MultipartBody.Part,id: String) : Call<Travel>?
}