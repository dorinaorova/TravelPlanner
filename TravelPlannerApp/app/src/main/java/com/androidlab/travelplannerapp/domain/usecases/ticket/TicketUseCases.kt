package com.androidlab.travelplannerapp.domain.usecases.ticket

import com.androidlab.travelplannerapp.data.model.Ticket
import com.androidlab.travelplannerapp.data.service.ticket.TicketService
import okhttp3.MultipartBody
import retrofit2.Call
import javax.inject.Inject

class GetTicketsByTravelIdUseCase @Inject constructor(private val ticketService: TicketService) {
    operator fun invoke(id: String): Call<List<Ticket>>? {
        return ticketService.getByTravelId(id)
    }
}

class GetTicketByIdUseCase @Inject constructor(private val ticketService: TicketService) {
    operator fun invoke(id: String): Call<Ticket>? {
        return ticketService.getById(id)
    }
}

class CreateTicketUseCase @Inject constructor(private val ticketService: TicketService) {
    operator fun invoke(ticket: Ticket): Call<Ticket>? {
        return ticketService.createTicket(ticket)
    }
}

class UploadTicketFileUseCase @Inject constructor(private val ticketService: TicketService) {
    operator fun invoke(file: MultipartBody.Part, id: String): Call<Ticket>? {
        return ticketService.uploadTicketFile(file, id)
    }
}

class DownloadTicketFileUseCase @Inject constructor(private val ticketService: TicketService) {
    operator fun invoke(fileName: String): Call<*>? {
        return ticketService.downloadTicketFile(fileName)
    }
}