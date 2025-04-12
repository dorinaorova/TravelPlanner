package com.dipterv.dipterv

import com.dipterv.dipterv.model.documentModel.Ticket
import com.dipterv.dipterv.repository.TicketRepository
import com.dipterv.dipterv.service.FileService
import com.dipterv.dipterv.service.TicketService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Paths
import java.util.*

class TicketServiceTest {

    private val ticketRepository: TicketRepository = mockk()
    private val fileService: FileService = mockk()
    private val ticketService = TicketService(ticketRepository, fileService)
    private val ticket = Ticket("testId", 1L, "userId","travelId","name", listOf("file1", "file2"))

    @Test
    fun whenGetById_ReturnsOneTicket(){
        every {ticketRepository.findById("testId")} returns Optional.of(ticket)

        val result = ticketService.findById("testId")
        assertEquals(ticket, result)
    }

    @Test
    fun whenUploadTicketFile_TheNameAddedToTheList(){
        val fileContent = "This is a test file".toByteArray()
        val mockFile = MockMultipartFile(
            "file",
            "file3.txt",
            "text/plain",
            fileContent
        )
        val updatedTicket = Ticket("testId", 1L, "userId","travelId","name", listOf("file1", "file2", "file3"))

        every {ticketRepository.findById("testId")} returns Optional.of(ticket)
        every {fileService.uploadFile(mockFile, Paths.get("travel/tickets"), "travelId")} returns "file3"
        every {ticketRepository.save(updatedTicket)} returns updatedTicket

        ticketService.uploadTicketFile("testId",mockFile )
        verify { ticketRepository.save(updatedTicket) }
    }
}