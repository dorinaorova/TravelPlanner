package com.androidlab.travelplannerapp.domain.usecases.ticket

import com.androidlab.travelplannerapp.data.model.Ticket
import com.androidlab.travelplannerapp.data.repository.TicketRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class GetTicketsByTravelIdUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    operator fun invoke(id: String): Call<List<Ticket>>? {
        return ticketRepository.getByTravelId(id)
    }
}

class GetTicketByIdUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    operator fun invoke(id: String): Call<Ticket>? {
        return ticketRepository.getById(id)
    }
}

class CreateTicketUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    operator fun invoke(ticket: Ticket): Call<Ticket>? {
        return ticketRepository.createTicket(ticket)
    }
}

class UploadTicketFileUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    operator fun invoke(file: MultipartBody.Part, id: String): Call<Ticket>? {
        return ticketRepository.uploadTicketFile(file, id)
    }
}

class DownloadTicketFileUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    operator fun invoke(fileName: String): Call<ResponseBody>? {
        return ticketRepository.downloadTicketFile(fileName)
    }
}