package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Ticket
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call

interface TicketRepository {

    fun getById(id: String) : Call<Ticket>?

    fun getByTravelId(id: String) : Call<List<Ticket>>?

    fun createTicket(ticket: Ticket) : Call<Ticket>?

    fun uploadTicketFile(file: MultipartBody.Part, id: String) : Call<Ticket>?

    fun downloadTicketFile(fileName: String) : Call<ResponseBody>?
}