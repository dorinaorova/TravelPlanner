package com.dipterv.dipterv

import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.repository.TravelRepository
import com.dipterv.dipterv.service.TravelService
import com.dipterv.dipterv.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.exp

class TravelServiceTest {

    private val travelRepository: TravelRepository = mockk()
    private val userService: UserService = mockk()
    private val travelService = TravelService(travelRepository, userService)
    private val travels = listOf(Travel("1", "name", 1748736000000L, 1748908800000L, "country", "city", 100, "HUF", "", listOf("tag1"), null, listOf("user2"), false, "user1", ),
                                Travel("2", "name", 1748736000000L, 1749504000000L, "country", "city", 300, "HUF", "", listOf("tag1"), null, null, true, "user1", ),
                                Travel("3", "name", 1748736000000L, 1748908800000L, "country", "city", 100, "HUF", "", listOf("tag1"), null, null, true, "user2", ))

    @Test
    fun whenGetById_ReturnsOneTravel(){
        every { travelRepository.findById("1") } returns Optional.of(travels[0])

        val result = travelService.findById("1")

        assertEquals(travels[0], result)
    }

    @Test
    fun getAllTravels_ReturnOnlyPublic(){
        every { travelRepository.findAll() } returns travels

        val result = travelService.findAllPublicTravels()

        assertTrue(result.all { it.public })
    }

    @Test
    fun getFilterValuesTest(){
        every { travelRepository.findAll() } returns travels

        val expectedResult = listOf(3,9,100, 300)
        val result = travelService.getFilterValues()

        assertEquals(expectedResult, result)
    }

    @Test
    fun calculateDaysTest(){
        val travel = travels[0]
        val endDate = travel.endDate
        val startDate = travel.startDate

        val result = travelService.calculateDurationInDays(endDate, startDate)
        val expectedResult = 3

        assertEquals(expectedResult, result)
    }

    @Test
    fun getParticipatedTravelsTest(){
        every { travelRepository.findAll() } returns travels

        val expectedResult = listOf(travels[0])

        val result = travelService.findParticipatedTravels("user2")

        assertEquals(expectedResult, result)
    }

    @Test
    fun addParticipantSuccessfully(){
        val updatedTravel = Travel("1", "name", 1748736000000L, 1748908800000L, "country", "city", 100, "HUF", "", listOf("tag1"), null, listOf("user2", "user3"), false, "user1", )
        every { travelRepository.findById("1") } returns Optional.of(travels[0])
        every { travelRepository.save(updatedTravel) } returns updatedTravel

        travelService.addParticipant("user3", "1")

        verify {travelRepository.save(updatedTravel)   }
    }

    @Test
    fun findMyTravelsTest(){
        every { travelRepository.findById("2") } returns Optional.of(travels[1])
        every { travelRepository.findById("3") } returns Optional.of(travels[2])

        every{ userService.findUserInfoDTOById("user1")} returns UserInfoDTO("user1","user1", "user1", "user1", null, null, null, listOf("2", "3"),null,null,null,null,null  )

        val expectedResult = listOf(travels[1], travels[2])

        val result = travelService.findMyTravels("user1")

        assertEquals(expectedResult, result)
    }
}