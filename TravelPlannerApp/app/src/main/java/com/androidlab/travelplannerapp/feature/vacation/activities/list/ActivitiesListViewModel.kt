package com.androidlab.travelplannerapp.feature.vacation.activities.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.domain.usecases.activities.CreateActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.DeleteActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class ActivitiesListViewModel @Inject constructor(
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
    private val createActivityUseCase: CreateActivityUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase
) : ViewModel(){
    private val _activities = mutableListOf<Activity>()
    var travelId: String = ""
    val activities: List<Activity> = _activities

    fun fetchData(id: String){
        viewModelScope.launch {
            travelId = id
        }
    }

    fun getActivities(){
        viewModelScope.launch {
            val call = getActivitiesByTravelIdUseCase(travelId)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                _activities.clear()
                _activities.addAll(response.body()!!)
            }
        }
    }

    fun addActivity(name: String, type : ActivityType){
        viewModelScope.launch {
            val activity = Activity(name = name, type = type)
            val call = createActivityUseCase(travelId, activity)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                getActivities()
            }
        }
    }

    fun deleteActivity(id: String){
        viewModelScope.launch {
             val call = deleteActivityUseCase(id)
             val response = call?.awaitResponse()
             if(response!!.isSuccessful){
                 getActivities()
             }
        }
    }
}