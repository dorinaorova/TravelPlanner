package com.dipterv.dipterv.serviceTests

import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.model.documentModel.spend.SpendType
import com.dipterv.dipterv.repository.SpendRepository
import com.dipterv.dipterv.service.SpendService
import com.dipterv.dipterv.service.Transaction
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class SpendServiceTest {

    private val spendRepository: SpendRepository = mockk()
    private val spendService = SpendService(spendRepository)
    private val spend = Spend("testId",1L, "userId", arrayOf("user1", "user2"), 100.0,SpendType.FOOD, "travelId")

    @Test
    fun whenGetById_ReturnsOneSpend(){
        every{spendRepository.findById("testId")} returns Optional.of(spend)

        val result = spendService.findById("testId")

        assertEquals(result, spend)
    }

    @Test
    fun calculateDeptTest(){
        every {spendRepository.findByTravelId("travelId")} returns listOf(spend)
        val expectedDept = mutableMapOf<String, Double>()
        expectedDept["user1"] = -50.0
        expectedDept["user2"] = -50.0
        expectedDept["userId"]=100.0

        val result = spendService.calculateDebt("travelId")

        assertEquals(expectedDept, result)
    }

    @Test
    fun calculateTransactionTest(){
        every {spendRepository.findByTravelId("travelId")} returns listOf(spend)
        val expectedDept =listOf(Transaction("user1", "userId", 50.0), Transaction("user2", "userId", 50.0))

        val result = spendService.calculateTransactions("travelId")

        assertEquals(expectedDept, result)
    }
}