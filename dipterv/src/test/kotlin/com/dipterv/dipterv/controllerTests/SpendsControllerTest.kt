package com.dipterv.dipterv.controllerTests

import com.dipterv.dipterv.controller.SpendsController
import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.model.documentModel.spend.SpendType
import com.dipterv.dipterv.security.JwtRequestFilter
import com.dipterv.dipterv.service.SpendService
import com.dipterv.dipterv.service.Transaction
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
    SpendsController::class, excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtRequestFilter::class])
    ])
@AutoConfigureMockMvc(addFilters = false)
class SpendsControllerTest(@Autowired val mockMvc: MockMvc)  {
    @MockBean
    private lateinit var spendService: SpendService

    private val objectMapper = ObjectMapper()

    private val spend =  Spend("testId",1L, "userId", arrayOf("user1", "user2"), 100.0, SpendType.FOOD, "travelId")
    private val spends = listOf(spend)
    private val transactions = listOf(Transaction("user1", "userId", 50.0), Transaction("user2", "userId", 50.0))

    @Test
    fun whenGetSpendsForTravel_ReturnsListWithStatus200() {
        `when`(spendService.findSpendsForTravel("travelId")).thenReturn(spends)

        mockMvc.perform(get("/spends/travel/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(spends)))
    }

    @Test
    fun whenAddSpendForTravel_ReturnsCreatedSpendWithStatus201() {
        `when`(spendService.addSpend(spend)).thenReturn(spend)

        mockMvc.perform(
            post("/spends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spend))
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun whenDeleteSpend_ReturnsStatus200() {
        doNothing().`when`(spendService).removeSpend("1")

        mockMvc.perform(delete("/spends/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun whenGetTransactions_ReturnsTransactionListWithStatus200() {
        `when`(spendService.calculateTransactions("travelId")).thenReturn(transactions)

        mockMvc.perform(get("/spends/transaction/travelId"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(transactions)))
    }
}