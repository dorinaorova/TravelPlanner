package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Ticket
import com.dipterv.dipterv.repository.TicketRepository
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths

@Service
class TicketService (val ticketRepository: TicketRepository, val travelService: TravelService, val fileService: FileService) {
    fun findById(id: String): Ticket {
        try {
            return ticketRepository.findById(id).get()
        }catch (ex:Exception){
            throw NotFoundException("Ticket with id $id not found")
        }
    }

    fun uploadTicketFile(id: String, file: MultipartFile ): Ticket {
        val ticket = this.findById(id)
        val ticketName = fileService.uploadFile(file,Paths.get("travel/tickets"), ticket.travelId)
        if(!ticket.files.contains(ticketName)){
            ticket.files = ticket.files.toMutableList().apply { add(ticketName) }
        }
        return ticketRepository.save(ticket)
    }

    fun createTicket(ticket: Ticket ): Ticket{
        return this.ticketRepository.save(ticket)
    }

    fun downloadTicketFile(fileName: String) : Resource {
        val file = fileService.downloadFile(Paths.get("travel/tickets"), fileName)
        return file
    }

    fun ticketsForTravel(travelId: String): List<Ticket> {
        return ticketRepository.findByTravelId(travelId)
    }
}