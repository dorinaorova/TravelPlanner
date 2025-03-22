package com.androidlab.travelplannerapp.feature.travel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class TravelViewModel @Inject constructor(
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
) : ViewModel()  {
    private var _travel = mutableStateOf(Travel())
    val markers = mutableStateListOf<Activity>()
    val mapLoading = mutableStateOf(true)

    val travel : Travel
        get() = _travel.value

    fun fetchData(id: String){
     viewModelScope.launch {
        val call = getTravelByIdUseCase(id)
         val response = call?.awaitResponse()
         if (response?.isSuccessful == true){
             _travel.value = response.body()!!
             getActivities()
         }
     }
    }

    fun ownTravel(context: Context) : Boolean{
        return getOwnUserId(context) == _travel.value.ownerId
    }

    private fun getActivities(){
        viewModelScope.launch {
            mapLoading.value = true
            val call = getActivitiesByTravelIdUseCase(_travel.value._id!!)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful) {
                markers.clear()
                markers.addAll(response.body()!!.filter { it.latitude != null && it.longitude != null })
                mapLoading.value = false
            }
        }
    }
}