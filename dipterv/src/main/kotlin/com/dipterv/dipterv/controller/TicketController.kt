package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.Ticket
import com.dipterv.dipterv.service.TicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/ticket")
class TicketController(private val ticketService: TicketService) {

    @GetMapping("/{id}")
    fun getTicketById(@PathVariable("id") id: String): ResponseEntity<Ticket?>{
        return ResponseEntity(ticketService.findById(id), HttpStatus.OK)
    }

    @GetMapping("/travel/{id}")
    fun getTicketsByTravelId(@PathVariable("id") id: String): ResponseEntity<List<Ticket>>{
        return ResponseEntity(this.ticketService.ticketsForTravel(id), HttpStatus.OK)
    }

    @PostMapping()
    fun createTicket(@RequestBody ticket: Ticket): ResponseEntity<*>{
        return ResponseEntity(this.ticketService.createTicket(ticket), HttpStatus.CREATED)
    }

    @PostMapping("/upload/{id}")
    fun uploadFile(@RequestBody file: MultipartFile, @PathVariable("id") id: String): ResponseEntity<*>{
        return ResponseEntity(this.ticketService.uploadTicketFile(id, file), HttpStatus.CREATED)
    }

    @GetMapping("/download/{fileName}")
    fun downloadFile(@PathVariable("fileName") fileName: String): ResponseEntity<*>{
    return ResponseEntity(this.ticketService.downloadTicketFile(fileName), HttpStatus.OK)
    }
}