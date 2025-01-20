package com.androidlab.travelplannerapp.data.travel

import com.androidlab.travelplannerapp.data.model.Travel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface TravelService {

    @Headers("Accept: application/json")
    @GET("travel/all")
    fun getAll() : Call<List<Travel>>?
}