package com.androidlab.travelplannerapp.feature.home

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.usecases.travel.GetMyTravelUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyTravelUseCase: GetMyTravelUseCase,
): ViewModel() {
    private var _myTravels = mutableStateOf<List<Travel>>(listOf())

    val myTravels: List<Travel>
        get(){
            return _myTravels.value
        }

    fun fetchTravels(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            if (!userId.isNullOrEmpty()) {

                val call = getMyTravelUseCase(userId)
                val response = call?.awaitResponse()
                if (response?.isSuccessful == true) {
                    _myTravels.value = response.body()!!
                }
            }
        }
    }

    fun getCurrentVacation(): Travel?{
        val currentVacation = _myTravels.value.firstOrNull({ it.isCurrent })
        return currentVacation
    }

    fun filterUpcomingTravels() : List<Travel>{
        val upcoming = _myTravels.value.filter{ it.startDate > System.currentTimeMillis() }
        return upcoming
    }
}