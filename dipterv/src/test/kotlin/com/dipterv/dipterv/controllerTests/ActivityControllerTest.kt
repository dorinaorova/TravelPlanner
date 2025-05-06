package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.ActivityController
import com.dipterv.dipterv.model.documentModel.activity.Activity
import com.dipterv.dipterv.model.documentModel.activity.ActivityType
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.ActivityService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    ActivityController::class, excludeFilters = [
    ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
])
@AutoConfigureMockMvc(addFilters = false)
class ActivityControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var activityService: ActivityService

    private val objectMapper = ObjectMapper()

    private val activity = Activity("testId","name", ActivityType.RESTAURANT, "travelId", false, null, null)
    private val activities = listOf(
        activity,
        Activity("testId2","name", ActivityType.RESTAURANT, "travelId", false, null, null)
    )

    @Test
    fun whenGetActivitiesForTravel_thenReturnsListWithStatus200() {
        `when`(activityService.findAllByTravelId("TravelId1")).thenReturn(activities)

        mockMvc.perform(get("/activity/travel/TravelId1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(activities)))
    }

    @Test
    fun whenCreateActivity_thenReturnsCreatedActivityWithStatus201() {
        `when`(activityService.createActivity(activity)).thenReturn(activity)

        mockMvc.perform(
            post("/activity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(activity))
        )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(activity)))
    }

    @Test
    fun whenDeleteActivity_thenReturnsStatus200AndMessage() {
        doNothing().`when`(activityService).deleteActivity("1")

        mockMvc.perform(delete("/activity/1"))
            .andExpect(status().isOk)
            .andExpect(content().string("deleted"))
    }

    @Test
    fun whenVisitActivity_thenReturnsUpdatedActivityWithStatus200() {
        val visitedActivity = activity.copy(visited = true)
        `when`(activityService.activityVisited("1")).thenReturn(visitedActivity)

        mockMvc.perform(get("/activity/visit/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(visitedActivity)))
    }
}