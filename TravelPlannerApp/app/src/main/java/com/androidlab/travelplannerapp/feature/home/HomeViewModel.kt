package com.androidlab.travelplannerapp.feature.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.Status
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.domain.usecases.invitation.AcceptInvitationUseCase
import com.androidlab.travelplannerapp.domain.usecases.invitation.GetInvitationsByUserIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.invitation.RejectInvitationUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetMyTravelUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetParticipatedTravels

import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyTravelUseCase: GetMyTravelUseCase,
    private val getInvitationsByUserIdUseCase: GetInvitationsByUserIdUseCase,
    private val searchTravelUseCase: SearchTravelUseCase,
    private val acceptInvitationUseCase: AcceptInvitationUseCase,
    private val rejectInvitationUseCase: RejectInvitationUseCase,
    private val getParticipatedTravels: GetParticipatedTravels
): ViewModel() {
    private var travels: List<Travel> = emptyList()
    private var _myTravels = mutableStateListOf<Travel>()
    private var _invitations = mutableStateOf<List<Invitation>>(listOf())


    val invitations: List<Invitation>
        get(){
            return _invitations.value
        }

    fun fetchTravels(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            if (!userId.isNullOrEmpty())
            {
                _myTravels.clear()
                val call = getMyTravelUseCase(userId)
                val response = call?.awaitResponse()
                if (response?.isSuccessful == true) {
                    _myTravels.addAll(response.body()!!)
                }
                val partCall = getParticipatedTravels(userId)
                val partResponse = partCall?.awaitResponse()
                if(partResponse?.isSuccessful == true){
                    _myTravels.addAll(partResponse.body()!!)
                }
                _myTravels.sortBy { it.startDate }
            }
        }
    }

    fun fetchInvitations(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            if (!userId.isNullOrEmpty()) {
                val call = getInvitationsByUserIdUseCase(userId)
                val response = call?.awaitResponse()
                if (response?.isSuccessful == true) {
                    _invitations.value = response.body()!!.filter { it.status == Status.PENDING }
                    val travelCall = searchTravelUseCase()
                    val travelResponse = travelCall?.awaitResponse()
                    if(travelResponse?.isSuccessful == true){
                        travels = travelResponse.body()!!
                    }
                }
            }
        }
    }

    fun getCurrentVacation(): Travel?{
        if(!_myTravels.isEmpty()){

        val currentVacation = _myTravels[0]
        return currentVacation
        }
        return null
    }

    fun filterUpcomingTravels() : List<Travel>{
        val now = System.currentTimeMillis()
        val upcoming = _myTravels.filter{  it.endDate>=now}
        return upcoming
    }

    fun findTravelNameById(travelId: String): String?{
        return travels.find { it._id == travelId }?.name
    }

    fun answerInvitation(id: String, isAccept: Boolean, context: Context){
        viewModelScope.launch {
            val call = if(isAccept){
                acceptInvitationUseCase(id)
            }else{
                rejectInvitationUseCase(id)
            }
            val response = call.awaitResponse()
            if(response.isSuccessful){
                fetchInvitations(context)
                fetchTravels(context)
            }
        }
    }
}