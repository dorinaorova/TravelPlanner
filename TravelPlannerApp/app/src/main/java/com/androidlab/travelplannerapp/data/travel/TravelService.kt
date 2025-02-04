package com.androidlab.travelplannerapp.data.travel

import com.androidlab.travelplannerapp.data.model.Travel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TravelService {

    @Headers("Accept: application/json")
    @GET("travel/all")
    fun getAll(@Query("name") name: String? = null, @Query("city") city: String? = null, @Query("country") country: String? = null, @Query("tags") tags: List<String>? = null) : Call<List<Travel>>?

    @Headers("Accept: application/json")
    @GET("travel/{id}")
    fun getById(@Path("id") id: String) : Call<Travel>?

    @Headers("Accept: application/json")
    @GET("travel/user/{id}")
    fun getTravelByUserId(@Path("id") id: String) : Call<List<Travel>>?

    @Headers("Accept: application/json")
    @POST("travel/user/{id}")
    fun newTravel(@Path("id") id: String, @Body travel: Travel) : Call<Travel>?

    @Headers("Accept: application/json")
    @PUT("travel/update")
    fun updateTravel(@Body travel: Travel) : Call<Travel>?
}