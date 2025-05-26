package com.androidlab.travelplannerapp.domain.usecases.activities

import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.repository.ActivityRepository
import retrofit2.Call
import javax.inject.Inject

class GetActivitiesByTravelIdUseCase @Inject constructor(private val repository: ActivityRepository) {
    operator fun invoke(id: String): Call<List<Activity>>? {
        return repository.getActivitiesByTravelId(id)
    }
}

class CreateActivityUseCase @Inject constructor(private val repository: ActivityRepository) {
    operator fun invoke(activity: Activity): Call<Activity>? {
        return repository.addActivity(activity)
    }
}

class DeleteActivityUseCase @Inject constructor(private val repository: ActivityRepository) {
    operator fun invoke(id: String): Call<Void>? {
        return repository.deleteActivity(id)
    }
}

class VisitActivityUseCase @Inject constructor(private val repository: ActivityRepository) {
    operator fun invoke(id: String): Call<Activity>? {
        return repository.visitActivity(id)
    }
}