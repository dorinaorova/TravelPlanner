package com.androidlab.travelplannerapp.data.service.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TravelService : TravelRepository {

    @Headers("Accept: application/json")
    @GET("travel/all")
    override fun getAll(@Query("name") name: String?,
               @Query("city") city: String?,
               @Query("country") country: String?,
               @Query("tags") tags: List<String>?,
               @Query("minDays") minDays:Int?,
               @Query("maxDays") maxDays: Int?,
               @Query("minPrice") minPrice:Int?,
               @Query("maxPrice") maxPrice: Int?) : Call<List<Travel>>?

    @Headers("Accept: application/json")
    @GET("travel/{id}")
    override fun getById(@Path("id") id: String) : Call<Travel>?

    @Headers("Accept: application/json")
    @GET("travel/user/{id}")
    override fun getTravelByUserId(@Path("id") id: String) : Call<List<Travel>>?

    @Headers("Accept: application/json")
    @POST("travel/user/{id}")
    override fun newTravel(@Path("id") id: String, @Body travel: Travel) : Call<Travel>?

    @Headers("Accept: application/json")
    @PUT("travel/update/{id}")
    override fun updateTravel(@Body travel: Travel, @Path("id") id: String) : Call<Travel>?

    @Headers("Accept: application/json")
    @GET("travel/filterValues")
    override fun getFilterValues() : Call<List<Int>>?

    @Headers("Accept: application/json")
    @GET("travel/user/{id}")
    override fun getMyTravel(@Path("id") id: String): Call<List<Travel>>?

    @Headers("Accept: application/json")
    @GET("travel/participate/{id}")
    override fun getParticipatedTravels(@Path("id") id: String): Call<List<Travel>>?
}