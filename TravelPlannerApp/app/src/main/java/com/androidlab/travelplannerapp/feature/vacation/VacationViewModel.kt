package com.androidlab.travelplannerapp.feature.vacation

import android.content.Context
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Ticket
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.Transaction
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.activities.GetActivitiesByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.payment.GetPaymentsByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.payment.GetTransactionsUseCase
import com.androidlab.travelplannerapp.domain.usecases.ticket.GetTicketsByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import com.androidlab.travelplannerapp.feature.utils.ownProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getUserByIdUseCase: GetUserDataUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getPaymentsByTravelIdUseCase: GetPaymentsByTravelIdUseCase,
    private val getTicketsUseCase: GetTicketsByTravelIdUseCase
    private val getActivitiesByTravelIdUseCase: GetActivitiesByTravelIdUseCase,
) : ViewModel() {
    private var _travel = mutableStateOf(Travel())
    private val _participants = mutableStateOf<List<UserInfo>>(emptyList())
    private var travel_id=""
    private val _ownTransaction = mutableStateOf<List<Transaction>>(emptyList())
    private val _ownDebt = mutableDoubleStateOf(0.0)
    private val _tickets = mutableStateListOf<Ticket>()
    val markers = mutableStateListOf<Activity>()
    val mapLoading = mutableStateOf(true)


    val travel : Travel
        get(){
            return _travel.value
        }
    val participants : List<UserInfo>
        get(){
            return _participants.value
        }
    val ownTransaction : List<Transaction>
        get(){
            return _ownTransaction.value
        }

    val ownDebt : Double
        get(){
            return _ownDebt.value
        }
    val tickets : List<Ticket>
        get() {
            return _tickets
        }

    fun fetchData(id: String, context: Context){
        viewModelScope.launch {
            travel_id = id
            val call = getTravelByIdUseCase(id)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true){
                _travel.value = response.body()!!
                if(!_travel.value.participantIds.isNullOrEmpty()){
                    _participants.value = emptyList()
                    for(id in _travel.value.participantIds!!){
                        if(!ownProfile(id, context)){
                            val userCall = getUserByIdUseCase(id)
                            val user = userCall?.awaitResponse()
                            if(user?.isSuccessful == true){
                                _participants.value += user.body()!!
                            }
                        }
                    }
                }
                if(!ownProfile(_travel.value.ownerId!!, context)) {
                    val ownerCall = getUserByIdUseCase(_travel.value.ownerId!!)
                    val owner = ownerCall?.awaitResponse()
                    if (owner?.isSuccessful == true) {
                        _participants.value += owner.body()!!
                    }
                }
                getTransactionForUser(context)
                getDebtForUser(context)
                getTickets()
                getActivities()
            }
        }
    }

    private fun getTransactionForUser(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = getTransactionsUseCase(travel_id)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                _ownTransaction.value = response.body()!!.filter { it.toUser == userId || it.fromUser == userId}
            }
        }
    }

    private fun getActivities(){
        viewModelScope.launch {
            mapLoading.value = true
            val call = getActivitiesByTravelIdUseCase(travel_id)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful) {
                markers.clear()
                markers.addAll(response.body()!!.filter { it.latitude != null && it.longitude != null })
                mapLoading.value = false
            }
        }
    }

    private fun getDebtForUser(context: Context){
        viewModelScope.launch {
            val userId = getOwnUserId(context)
            val call = getPaymentsByTravelIdUseCase(travel_id)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                val payments = response.body()!!.filter { it.userId == userId || it.partUserIds.contains(userId) }
                payments.forEach {
                    if(it.userId == userId){
                        _ownDebt.value += it.cost
                    }
                    if(it.partUserIds.contains(userId)){
                        _ownDebt.value -= it.cost/it.partUserIds.size
                    }
                }
            }
        }
    }

    private fun getTickets(){
        viewModelScope.launch {
            _tickets.clear()
            val call = getTicketsUseCase(travel_id)
            val response = call?.awaitResponse()
            if(response!!.isSuccessful){
                _tickets.addAll(response.body()!!)
                _tickets.sortBy { it.date }
            }
        }
    }

    fun findUserName(id: String, context: Context ): String{
        if(ownProfile(id, context)) return "You"
        else if(_participants.value.find { it._id == id } != null) return _participants.value.find { it._id == id }!!.username
        return "?"
    }
}