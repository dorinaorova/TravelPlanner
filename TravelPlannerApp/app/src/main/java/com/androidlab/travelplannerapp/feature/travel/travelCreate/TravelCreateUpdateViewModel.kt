package com.androidlab.travelplannerapp.feature.travel.travelCreate

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.NewTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.UpdateTravelUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class TravelCreateUpdateViewModel @Inject constructor(
    private val newTravelUseCase: NewTravelUseCase,
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val updateTravelUseCase: UpdateTravelUseCase
): ViewModel(){
    var tagList =  mutableStateListOf<String>()
    private var _travel = mutableStateOf(Travel())

    val travel: Travel
        get() = _travel.value

    fun save(travel: Travel, context: Context, navController: NavController){
        viewModelScope.launch {
            val userId = getOwnUserId(context)!!
            travel.tags = tagList
            val call = newTravelUseCase(userId, travel)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                navController.navigate(Screen.ProfileScreen.route)
            }
        }
    }

    fun update(travel: Travel, navController: NavController){
        viewModelScope.launch {
            val call = updateTravelUseCase(travel)
            travel.tags = tagList
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                navController.navigate(Screen.TravelProfileScreen.route+"?id=${travel._id}")
            }
        }
    }

    fun verifyTravelForm(travel: Travel) : Boolean{
        val nameValid = travel.name.isNotEmpty()
        val cityValid = travel.city.isNotEmpty()
        val countryValid = travel.country.isNotEmpty()
        val startDateValid = travel.startDate != 0L
        val endDateValid = travel.endDate != 0L
        val priceValid = travel.price > 0
        return nameValid && cityValid && countryValid && startDateValid && endDateValid && priceValid && travel.endDate > travel.startDate
    }

    fun fetchData(id: String){
        viewModelScope.launch {
            tagList.clear()
            val call = getTravelByIdUseCase(id)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _travel.value = response.body()!!
                if(_travel.value.tags != null) tagList.addAll(_travel.value.tags!!)
            }
        }
    }
}