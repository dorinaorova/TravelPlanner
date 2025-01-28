package com.androidlab.travelplannerapp.feature.travelCreate

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.usecases.travel.NewTravelUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class TravelCreateUpdateViewModel @Inject constructor(
    private val newTravelUseCase: NewTravelUseCase
): ViewModel(){
    private var _travel = mutableStateOf(Travel("", "", 0L, 0L, "", "", 0,"EUR", null, null, null, false, null))

    val travel: Travel
        get() = _travel.value

    fun save(travel: Travel, context: Context){
        viewModelScope.launch {
            Log.d("TRAVEL", travel.toString())
            val userId = getOwnUserId(context)!!
            val call = newTravelUseCase(userId, travel)
            val response = call?.awaitResponse()
            Log.d("RESPONSE", response.toString())
            if(response?.isSuccessful == true){
                //TODO
            }
        }
    }
}