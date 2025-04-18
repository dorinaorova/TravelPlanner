package com.dipterv.dipterv.controllerTests


import com.dipterv.dipterv.controller.InvitationController
import com.dipterv.dipterv.model.documentModel.InvitationStatus
import com.dipterv.dipterv.model.documentModel.TravelInvitation
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.InvitationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(
    InvitationController::class, excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
    ])
@AutoConfigureMockMvc(addFilters = false)
class InvitationControllerTest (@Autowired val mockMvc: MockMvc)  {
    @MockBean
    private lateinit var invitationService: InvitationService

    private val objectMapper = ObjectMapper()

    private val invitation = TravelInvitation("1", "userId", "travelId", InvitationStatus.PENDING )
    private val invitations = listOf(
        invitation,
        TravelInvitation("2", "userId", "travelId", InvitationStatus.PENDING)
    )

    @Test
    fun whenGetByUserId_ReturnsList_WithStatus200() {
        `when`(invitationService.findByUserId("userId")).thenReturn(invitations)

        mockMvc.perform(get("/invitation/user/userId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(invitations)))
    }

    @Test
    fun whenGetByTravelId_ReturnsList_WithStatus200() {
        `when`(invitationService.findByTravelId("travelId")).thenReturn(invitations)

        mockMvc.perform(get("/invitation/travel/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(invitations)))
    }

    @Test
    fun whenCreateInvitation_ReturnsCreatedStatus201() {
        `when`(invitationService.createInvitation(invitation)).thenReturn(invitation)

        mockMvc.perform(
            post("/invitation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invitation))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(invitation)))
    }

    @Test
    fun whenAcceptInvitation_ReturnsStatus200() {
        `when`(invitationService.acceptInvitation("1")).thenReturn(invitation)

        mockMvc.perform(get("/invitation/accept/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun whenRejectInvitation_ReturnsStatus200() {
        `when`(invitationService.declineInvitation("1")).thenReturn(invitation)

        mockMvc.perform(get("/invitation/reject/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun whenDeleteInvitation_ReturnsStatus200() {
        doNothing().`when`(invitationService).deleteInvitation("1")

        mockMvc.perform(delete("/invitation/1"))
            .andExpect(status().isOk)
    }
}