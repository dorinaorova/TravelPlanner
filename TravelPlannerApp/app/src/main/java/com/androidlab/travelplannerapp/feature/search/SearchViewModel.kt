package com.androidlab.travelplannerapp.feature.search

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor( private val searchTravelUseCase: SearchTravelUseCase,
    private val searchUserUseCase: SearchUserUseCase
)  : ViewModel() {
    private var _travels = mutableStateListOf<Travel>()
    val travel: List<Travel>
        get() = _travels

    private var _users = mutableStateListOf<UserInfo>()
    val users : List<UserInfo>
        get() = _users

    fun getAllTravel(){
        viewModelScope.launch{
            val call = searchTravelUseCase()
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _travels.clear()
                _travels.addAll(response.body()!!)
            }

        }
    }

    fun getAllUsers(){
        viewModelScope.launch{
            _users.clear()
            val call = searchUserUseCase()
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _users.clear()
                _users.addAll(response.body()!!)

            }
        }
    }

    fun searchUser(name: String){
        viewModelScope.launch {
            _users.clear()
            val call = searchUserUseCase(name)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _users.addAll(response.body()!!)
            }
        }
    }

    fun filterTravel(country: String, city: String,  priceSliderPosition:
    ClosedFloatingPointRange<Float>, daysSliderPosition:
    ClosedFloatingPointRange<Float>, tagList: List<String>){
        viewModelScope.launch {
            Log.d("TAG", "filterTravel: $country $city $priceSliderPosition $daysSliderPosition $tagList")
            val call = searchTravelUseCase(name = null, city = city, country = country, tags = tagList)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _travels.clear()
                _travels.addAll(response.body()!!)
            }
        }
    }

}