package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.SpendsController
import com.dipterv.dipterv.controller.TicketController
import com.dipterv.dipterv.model.documentModel.Ticket
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.TicketService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(
    TicketController::class, excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
    ])
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest (@Autowired val mockMvc: MockMvc){
    @MockBean
    private lateinit var ticketService: TicketService

    private val objectMapper = ObjectMapper()

    private val ticket = Ticket("testId", 1L, "userId","travelId","name", listOf("file1", "file2"))
    private val tickets = listOf(ticket)

    @Test
    fun whenGetTicketById_ReturnsTicketWithStatus200() {
        `when`(ticketService.findById("1")).thenReturn(ticket)

        mockMvc.perform(get("/ticket/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(ticket)))
    }

    @Test
    fun whenGetTicketsByTravelId_ReturnsListOfTicketsWithStatus200() {
        `when`(ticketService.ticketsForTravel("travelId")).thenReturn(tickets)

        mockMvc.perform(get("/ticket/travel/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(tickets)))
    }

    @Test
    fun whenCreateTicket_ReturnsCreatedTicketWithStatus201() {
        `when`(ticketService.createTicket(ticket)).thenReturn(ticket)

        mockMvc.perform(
            post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(ticket)))
    }

    @Test
    fun whenUploadFile_ReturnsCreatedResponseWithStatus201() {
        val file = MockMultipartFile("file", "ticket.pdf", "application/pdf", "PDF content".toByteArray())
        val response =ticket

        `when`(ticketService.uploadTicketFile("1", file)).thenReturn(response)

        mockMvc.perform(
            multipart("/ticket/upload/1")
                .file(file)
                .with { it.method = "POST"; it }
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(response)))
    }

    @Test
    fun whenDownloadFile_ReturnsPdfFileWithStatus200() {
        val resource = ByteArrayResource("PDF content".toByteArray())

        `when`(ticketService.downloadTicketFile("ticket.pdf")).thenReturn(resource)

        mockMvc.perform(get("/ticket/download/ticket.pdf"))
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/pdf"))
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ticket.pdf\""))
            .andExpect(content().bytes("PDF content".toByteArray()))
    }
}
