package com.androidlab.travelplannerapp.data.service.activities

import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.repository.ActivityRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityService : ActivityRepository{
    @Headers("Accept: application/json")
    @GET("activity/travel/{id}")
    override fun getActivitiesByTravelId(@Path("id") id: String) : Call<List<Activity>>?

    @Headers("Accept: application/json")
    @POST("activity")
    override fun addActivity(@Body activity: Activity) : Call<Activity>?

    @Headers("Accept: application/json")
    @DELETE("activity/{id}")
    override fun deleteActivity(@Path("id") id: String) : Call<Void>?

    @Headers("Accept: application/json")
    @GET("activity/visit/{id}")
    override fun visitActivity(@Path("id") id: String) : Call<Activity>?
}
