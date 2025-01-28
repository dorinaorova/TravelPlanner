package com.androidlab.travelplannerapp.data.travel

import com.androidlab.travelplannerapp.data.model.Travel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface TravelService {

    @Headers("Accept: application/json")
    @GET("travel/all")
    fun getAll() : Call<List<Travel>>?

    @Headers("Accept: application/json")
    @POST("travel/user/{id}")
    fun newTravel(@Path("id") id: String, @Body travel: Travel) : Call<Travel>?
}