package com.androidlab.travelplannerapp.data.service.travel

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
    fun getAll(@Query("name") name: String? = null,
               @Query("city") city: String? = null,
               @Query("country") country: String? = null,
               @Query("tags") tags: List<String>? = null,
               @Query("minDays") minDays:Int? = null,
               @Query("maxDays") maxDays: Int? =null,
               @Query("minPrice") minPrice:Int? = null,
               @Query("maxPrice") maxPrice: Int? = null) : Call<List<Travel>>?

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
    @PUT("travel/update/{id}")
    fun updateTravel(@Body travel: Travel, @Path("id") id: String) : Call<Travel>?

    @Headers("Accept: application/json")
    @GET("travel/filterValues")
    fun getFilterValues() : Call<List<Int>>?

    @Headers("Accept: application/json")
    @GET("travel/user/{id}")
    fun getMyTravel(@Path("id") id: String): Call<List<Travel>>?

    @Headers("Accept: application/json")
    @GET("travel/participate/{id}")
    fun getParticipatedTravels(@Path("id") id: String): Call<List<Travel>>?
}