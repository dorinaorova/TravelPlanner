package com.androidlab.travelplannerapp.feature.search

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.FollowRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.IsFollowerUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import com.androidlab.travelplannerapp.feature.utils.isFollower
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor( private val searchTravelUseCase: SearchTravelUseCase,
    private val searchUserUseCase: SearchUserUseCase
)  : ViewModel() {

    private var _users = mutableStateListOf<UserInfo>()
    val users : List<UserInfo>
        get() = _users

    fun getAllTravel(){
        viewModelScope.launch{
            val call = searchTravelUseCase()
            val response = call?.awaitResponse()
        }
    }

    fun getAllUsers(){
        viewModelScope.launch{
            _users.clear()
            val call = searchUserUseCase()
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
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

}