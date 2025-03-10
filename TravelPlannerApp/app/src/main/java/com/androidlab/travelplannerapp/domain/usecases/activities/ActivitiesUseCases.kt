package com.androidlab.travelplannerapp.domain.usecases.activities

import com.androidlab.travelplannerapp.data.activities.ActivityService
import com.androidlab.travelplannerapp.data.model.Activity
import retrofit2.Call
import javax.inject.Inject

class GetActivitiesByTravelIdUseCase @Inject constructor(private val service: ActivityService) {
    operator fun invoke(id: String): Call<List<Activity>>? {
        return service.getActivitiesByTravelId(id)
    }
}

class CreateActivityUseCase @Inject constructor(private val service: ActivityService) {
    operator fun invoke(id: String, activity: Activity): Call<Activity>? {
        return service.addActivity(id, activity)
    }
}

class UpdateActivityUseCase @Inject constructor(private val service: ActivityService) {
    operator fun invoke(id: String, activity: Activity): Call<Activity>? {
        return service.updateActivity(id, activity)
    }
}

class DeleteActivityUseCase @Inject constructor(private val service: ActivityService) {
    operator fun invoke(id: String): Call<Void>? {
        return service.deleteActivity(id)
    }
}