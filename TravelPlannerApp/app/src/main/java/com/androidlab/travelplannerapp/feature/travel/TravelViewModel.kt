package com.androidlab.travelplannerapp.feature.travel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.IsTravelLikedUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.LikeTravelUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class TravelViewModel @Inject constructor(
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
    private val likeTravelUseCase: LikeTravelUseCase,
    private val isTravelLikedUseCase: IsTravelLikedUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel()  {
    private var _travel = mutableStateOf(Travel())
    private var _owner = mutableStateOf(UserInfo("","","","","","", "", emptyList(), "", "", emptyList(), emptyList(), emptyList()))
    var liked = mutableStateOf(false)
    val markers = mutableStateListOf<Activity>()
    val mapLoading = mutableStateOf(true)

    val travel : Travel
        get() = _travel.value

    val user: UserInfo
        get()= _owner.value


    fun fetchData(id: String, context: Context){
     viewModelScope.launch {
        val call = getTravelByIdUseCase(id)
         val response = call?.awaitResponse()
         if (response?.isSuccessful == true){
             _travel.value = response.body()!!
             getActivities()
             getOwnerData()
             isTravelLiked(context)
         }
     }
    }

    private fun getOwnerData(){
        viewModelScope.launch {
            val call = getUserDataUseCase(_travel.value.ownerId!!)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _owner.value = response.body()!!
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

    fun likeTravel(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = likeTravelUseCase(userId!!, _travel.value._id!!)
            val response = call?.awaitResponse()
            if (response!!.isSuccessful){
                isTravelLiked(context)
            }
        }
    }

    private fun isTravelLiked(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = isTravelLikedUseCase(userId!!, _travel.value._id!!)
            val response = call?.awaitResponse()
            if (response!!.isSuccessful) {
                liked.value = response.body()!!
            }
        }
    }
}