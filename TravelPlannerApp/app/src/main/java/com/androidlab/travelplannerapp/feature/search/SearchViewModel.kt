package com.androidlab.travelplannerapp.feature.search

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.LikeTravelUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchTravelUseCase: SearchTravelUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val likeTravelUseCase: LikeTravelUseCase
)  : ViewModel() {
    private val baseFilterValues = mutableStateListOf<Int>(0,0,0,0)
    var country = mutableStateOf("")
    var city = mutableStateOf("")
    var priceSliderPosition = mutableStateOf(0f..100f)
    var daysSliderPosition = mutableStateOf(0f..100f)
    var tagList =  mutableStateListOf<String>()
    var likedTravelList = mutableStateListOf<String>()
    val ownTravelList = mutableStateListOf<String>()

    var searchName = mutableStateOf("")

    private var _travels = mutableStateListOf<Travel>()
    val travel: List<Travel>
        get() = _travels

    private var _users = mutableStateListOf<UserInfo>()
    val users : List<UserInfo>
        get() = _users

    fun getOwnUserData(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = getUserDataUseCase(userId!!)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                likedTravelList.clear()
                ownTravelList.clear()
                response.body()!!.likedTravelIds?.let { likedTravelList.addAll(it) }
                response.body()!!.travelIds?.let { ownTravelList.addAll(it) }
            }
        }
    }

    fun likeTravel(travelId: String, context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = likeTravelUseCase(userId!!, travelId)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                getOwnUserData(context)
            }
        }
    }

    fun isTravelLiked(travelId: String): Boolean{
        return likedTravelList.contains(travelId)
    }

    fun isTravelOwn(travelId: String): Boolean{
        return ownTravelList.contains(travelId)
    }

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

    fun searchUser(){
        viewModelScope.launch {
            _users.clear()
            val call = searchUserUseCase(searchName.value)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _users.addAll(response.body()!!)
            }
        }
    }

    fun searchTravel(){
        viewModelScope.launch {
            _travels.clear()
            val call = searchTravelUseCase(name = searchName.value)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _travels.addAll(response.body()!!)
            }
        }
    }

    fun filterTravel(){
        viewModelScope.launch {
            val call = searchTravelUseCase(name = searchName.value, city = city.value, country = country.value, tags = tagList,
                minDays = calculateMinDays(), maxDays = calculateMaxDays(), minPrice = calculateMinPrice(), maxPrice = calculateMaxPrice())
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _travels.clear()
                _travels.addAll(response.body()!!)
            }
        }
    }


    fun priceFilterValue(): String{
        return "${calculateMinPrice()} - ${calculateMaxPrice()}"
    }

    fun daysFilterValue(): String{
        return "${calculateMinDays()} - ${calculateMaxDays()}"
    }

    fun calculateSteps(days: Boolean) : Int{
        return if(baseFilterValues.isEmpty()){
            1
        }
        else if(days){
            maxOf(baseFilterValues[1] - 1, 1)
        }else{
            maxOf(baseFilterValues[3] - 1, 1)
        }
    }

    private fun calculateMinDays() : Int{
        return (baseFilterValues[1] * (daysSliderPosition.value.start)/100).toInt()
    }

    private fun calculateMaxDays() : Int{
        return (baseFilterValues[1] *daysSliderPosition.value.endInclusive/100).toInt()
    }

    private fun calculateMinPrice() : Int{
        return (baseFilterValues[3] *priceSliderPosition.value.start/100).toInt()
    }

    private fun calculateMaxPrice() : Int{
        return (baseFilterValues[3] *priceSliderPosition.value.endInclusive/100).toInt()

    }
}