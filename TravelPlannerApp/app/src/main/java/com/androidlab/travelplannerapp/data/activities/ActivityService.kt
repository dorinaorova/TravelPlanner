package com.androidlab.travelplannerapp.data.activities

import com.androidlab.travelplannerapp.data.model.Activity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityService {
    @Headers("Accept: application/json")
    @Multipart
    @GET("activities/travel/{id}")
    fun getActivitiesByTravelId(@Path("id") id: String) : Call<List<Activity>>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("activities/travel/{id}")
    fun addActivity(@Path("id") id: String, @Body activity: Activity) : Call<Activity>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("activities/{id}")
    fun updateActivity(@Path("id") id: String, @Body activity: Activity) : Call<Activity>?

    @Headers("Accept: application/json")
    @Multipart
    @DELETE("activities/{id}")
    fun deleteActivity(@Path("id") id: String) : Call<Void>?

}
