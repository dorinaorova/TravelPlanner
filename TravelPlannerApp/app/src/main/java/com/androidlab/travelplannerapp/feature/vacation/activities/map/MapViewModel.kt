package com.androidlab.travelplannerapp.feature.vacation.activities.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.domain.usecases.activities.CreateActivityUseCase
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
    private val createActivityUseCase: CreateActivityUseCase
) : ViewModel() {

    var ownTravel: Boolean = false
    var travelId: String = ""
    var selectedCoords : LatLng? = null
    val markers = mutableStateListOf<Activity>()
    val loading = mutableStateOf(true)

    private fun ownTravel(context: Context){
        viewModelScope.launch {
            val call = getTravelByIdUseCase(travelId)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true){
                ownTravel = response.body()!!.ownerId == getOwnUserId(context)
            }
        }
    }

    fun fetchData(id: String, context: Context){
        viewModelScope.launch {
            travelId = id
            loading.value=true
            getActivities()
            ownTravel(context)
        }
    }
    private fun getActivities(){
        viewModelScope.launch {
            val call = getActivitiesByTravelIdUseCase(travelId)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                markers.clear()
                markers.addAll(response.body()!!.filter { it.xcoord != null && it.ycoord != null })
                loading.value = false
            }
        }
    }


    fun addActivity(name: String, type: ActivityType){
        viewModelScope.launch {
            val activity = Activity(name = name, type = type, travelId = travelId, visited = false, xcoord = selectedCoords?.latitude, ycoord = selectedCoords?.longitude)
            val call = createActivityUseCase(activity)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                getActivities()
            }
        }
    }

    fun coordsSelected(coords: LatLng){
        selectedCoords = coords
    }
}