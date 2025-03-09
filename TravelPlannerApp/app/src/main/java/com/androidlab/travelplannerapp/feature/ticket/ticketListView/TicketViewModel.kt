package com.androidlab.travelplannerapp.feature.ticket.ticketListView

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Ticket
import com.androidlab.travelplannerapp.domain.usecases.ticket.CreateTicketUseCase
import com.androidlab.travelplannerapp.domain.usecases.ticket.GetTicketsByTravelIdUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
   private val getTicketsByTravelIdUseCase: GetTicketsByTravelIdUseCase,
    private val createTicketUseCase: CreateTicketUseCase
) : ViewModel() {
    private val _tickets = mutableStateListOf<Ticket>()
    private var travelId: String = ""

    val tickets: List<Ticket>
        get() = _tickets

    fun setTravelId(id: String){
        travelId = id
    }
    fun fetchData(){
        viewModelScope.launch {
            val call = getTicketsByTravelIdUseCase(travelId)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _tickets.clear()
                _tickets.addAll(response.body()!!)
                _tickets.sortBy { it.date }
            }
        }
    }

    fun createTicket(name: String, date: Long, context: Context){
        val ticket = Ticket(name=name, date=date, userId = getOwnUserId(context)!!, travelId = travelId, files = emptyList())
        viewModelScope.launch {
            val call = createTicketUseCase(ticket)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                fetchData()
            }

        }
    }
}