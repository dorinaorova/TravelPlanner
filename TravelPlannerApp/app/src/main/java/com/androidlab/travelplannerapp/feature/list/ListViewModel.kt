package com.androidlab.travelplannerapp.feature.list

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByUserIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getTravelByUserIdUseCase: GetTravelByUserIdUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    val travels = mutableListOf<Travel>()
    val users = mutableListOf<UserInfo>()

    fun getList(type: String, userId:String, context : Context){
        viewModelScope.launch {
            if(type == "travel"){
                val call = getTravelByUserIdUseCase(userId)
                val result = call?.awaitResponse()
                if(result!!.isSuccessful){
                    travels.clear()
                    travels.addAll(result.body()!!)
                }
            }else{
                val userCall = getUserDataUseCase(userId)
                val userResponse = userCall?.awaitResponse()
                if(userResponse!!.isSuccessful){
                    val user = userResponse.body()!!
                    if(type == "follower"){
                        val followerIds = user.followerIds
                        users.clear()
                        for(id in followerIds?.iterator()!!){
                            val call = getUserDataUseCase(id)
                            val response = call?.awaitResponse()
                            if(response!!.isSuccessful){
                                users.add(response.body()!!)
                            }
                        }
                    }else if(type == "following"){
                        val followingIds = user.followingIds
                        users.clear()
                        for(id in followingIds?.iterator()!!){
                            val call = getUserDataUseCase(id)
                            val response = call?.awaitResponse()
                            if(response!!.isSuccessful){
                                users.add(response.body()!!)
                            }
                        }
                    }else{
                        val likedTravelIds = user.likedTravelIds
                        travels.clear()
                        for(id in likedTravelIds?.iterator()!!){
                            val call = getTravelByIdUseCase(id)
                            val response = call?.awaitResponse()
                            if(response!!.isSuccessful){
                                travels.add(response.body()!!)
                            }
                        }
                    }
                }
            }
        }
    }

    fun likeTravel(travelId: String, context: Context){

    }
}