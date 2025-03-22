package com.androidlab.travelplannerapp.feature.vacation.activities.list

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.domain.usecases.activities.CreateActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.DeleteActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.VisitActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class ActivitiesListViewModel @Inject constructor(
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
    private val createActivityUseCase: CreateActivityUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val visitActivityUseCase: VisitActivityUseCase,
    private val getTravelByIdUseCase: GetTravelByIdUseCase
) : ViewModel(){
    private val _activities = mutableStateListOf<Activity>()
    var travelId: String = ""
    val activities: List<Activity> = _activities
    var ownTravel= mutableStateOf(true)

    fun fetchData(id: String, context: Context){
        viewModelScope.launch {
            travelId = id
            ownTravel(context)
            getActivities()
        }
    }

    private fun ownTravel(context: Context){
        viewModelScope.launch {
            val call = getTravelByIdUseCase(travelId)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true){
                ownTravel.value = response.body()!!.ownerId == getOwnUserId(context)
            }
        }
    }

    private fun getActivities(){
        viewModelScope.launch {
            val call = getActivitiesByTravelIdUseCase(travelId)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                _activities.clear()
                _activities.addAll(response.body()!!)
                sortActivities()
            }
        }
    }

    private fun sortActivities(){
        _activities.sortBy { it.visited }
    }

    fun addActivity(name: String, type : ActivityType){
        viewModelScope.launch {
            val activity = Activity(name = name, type = type, travelId = travelId, visited = false)
            val call = createActivityUseCase(activity)
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

    fun visitActivity(id: String){
        viewModelScope.launch {
            val call = visitActivityUseCase(id)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                Log.d("Activity visited", response.body().toString())
                val newActivity = response.body()
                if (newActivity != null) {
                    _activities[_activities.indexOfFirst { it.id == newActivity.id }] = newActivity
                    sortActivities()
                }
            }
        }
    }
}