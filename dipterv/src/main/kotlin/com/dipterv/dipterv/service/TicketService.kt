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

    fun saveTicket(ticket: Ticket, file: MultipartFile, travelId: String ): Ticket {
        val ticketName = fileService.uploadFile(file,Paths.get("travel/tickets"), travelId)
        ticket.fileName = ticketName
        val newTicket = ticketRepository.save(ticket)
        travelService.uploadTicket(travelId, newTicket._id)
        return newTicket
    }

    fun findTicketById(id: String) : Resource {
        val ticketPaths = findById(id).fileName
        val file = fileService.downloadFile(Paths.get("travel/tickets"), ticketPaths)
        return file
    }

    fun ticketsForTravel(travelId: String): List<Ticket> {
        val ticketIds = travelService.getById(travelId).ticketIds
        val tickets = mutableListOf<Ticket>()
        ticketIds.forEach { id ->
            tickets.add(findById(id) )
        }
        return tickets.toList()
    }
}