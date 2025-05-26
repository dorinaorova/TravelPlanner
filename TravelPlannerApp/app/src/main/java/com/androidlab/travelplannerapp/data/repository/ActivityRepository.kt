package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Activity
import retrofit2.Call

interface ActivityRepository {

    fun getActivitiesByTravelId(id: String) : Call<List<Activity>>?

    fun addActivity(activity: Activity) : Call<Activity>?

    fun deleteActivity(id: String) : Call<Void>?

    fun visitActivity(id: String) : Call<Activity>?
}