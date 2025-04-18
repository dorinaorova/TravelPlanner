package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.TravelController
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.requestModel.TravelUpdateRequest
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.FileService
import com.dipterv.dipterv.service.TicketService
import com.dipterv.dipterv.service.TravelService
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
import java.nio.file.Paths

@WebMvcTest(
    TravelController::class, excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
    ])
@AutoConfigureMockMvc(addFilters = false)
class TravelControllerTest  (@Autowired val mockMvc: MockMvc){
    @MockBean
    private lateinit var travelService: TravelService

    @MockBean
    private lateinit var fileService: FileService

    @MockBean
    private lateinit var ticketService: TicketService

    private val objectMapper = ObjectMapper()

    private val travel = Travel("1", "name1", 1748736000000L, 1748908800000L, "country1", "city1", 200, "HUF", "", listOf("tag1"), null, listOf("user2"), false, "user1",)
    private val travels = listOf(travel)

    @Test
    fun whenGetAllTravels_ReturnsFilteredTravelsWithStatus200() {
        `when`(travelService.findAllPublicTravels()).thenReturn(travels)

        mockMvc.perform(get("/travel/all"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(travels)))
    }

    @Test
    fun whenGetFilterValues_ReturnsOk() {
        val values = listOf(1,6, 200, 200)
        `when`(travelService.getFilterValues()).thenReturn(values)

        mockMvc.perform(get("/travel/filterValues"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(values)))
    }

    @Test
    fun whenGetMyTravels_ReturnsUserTravelsWithStatus200() {
        `when`(travelService.findMyTravels("user1")).thenReturn(travels)

        mockMvc.perform(get("/travel/user/user1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(travels)))
    }

    @Test
    fun whenGetParticipatedTravels_ReturnsTravelsWithStatus200() {
        `when`(travelService.findParticipatedTravels("user1")).thenReturn(travels)

        mockMvc.perform(get("/travel/participate/user1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(travels)))
    }

    @Test
    fun whenGetTravelById_ReturnsTravelWithStatus200() {
        `when`(travelService.findById("1")).thenReturn(travel)

        mockMvc.perform(get("/travel/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(travel)))
    }

    @Test
    fun whenPostNewTravel_ReturnsCreatedTravelWithStatus201() {
        `when`(travelService.createNew("user1", travel)).thenReturn(travel)

        mockMvc.perform(
            post("/travel/user/user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(travel))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(travel)))
    }

    @Test
    fun whenUpdateTravel_ReturnsUpdatedTravelWithStatus200() {
        val updateRequest = TravelUpdateRequest(null, null, null, "updatedCountry", null, null, null, null, null, null)
        val updatedTravel = travel.copy(name = "Updated Trip")

        `when`(travelService.update("1", updateRequest)).thenReturn(updatedTravel)

        mockMvc.perform(
            put("/travel/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedTravel)))
    }

    @Test
    fun whenUploadImage_ReturnsUpdatedTravelWithStatus200() {
        val file = MockMultipartFile("file", "image.jpg", "image/jpeg", "IMAGE_DATA".toByteArray())
        val fileName = "image.jpg"
        val updatedTravel = travel.copy(pictureFileName = fileName)

        `when`(fileService.uploadFile(file, Paths.get("travel/images"), "1")).thenReturn(fileName)
        `when`(travelService.uploadImage(fileName, "1")).thenReturn(updatedTravel)

        mockMvc.perform(
            multipart("/travel/image/upload/1")
                .file(file)
                .with { it.method = "POST"; it }
        )
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(updatedTravel)))
    }

    @Test
    fun whenDownloadImage_ReturnsImageWithStatus200() {
        val imageBytes = "IMAGE_DATA".toByteArray()
        val resource = ByteArrayResource(imageBytes)

        `when`(fileService.downloadFile(Paths.get("travel/images"), "image.jpg")).thenReturn(resource)

        mockMvc.perform(get("/travel/image/download/image.jpg"))
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE))
            .andExpect(content().bytes(imageBytes))
    }
}