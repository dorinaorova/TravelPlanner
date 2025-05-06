package com.dipterv.dipterv.serviceTests

import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.dto.UserInfoDTO
import com.dipterv.dipterv.model.requestModel.TravelUpdateRequest
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
class TravelServiceTest {

    private val travelRepository: TravelRepository = mockk()
    private val userService: UserService = mockk()
    private val travelService = TravelService(travelRepository, userService)
    private val travels = listOf(Travel("1", "name1", 1748736000000L, 1748908800000L, "country1", "city1", 200, "HUF", "", listOf("tag1"), null, listOf("user2"), false, "user1", ),
                                Travel("2", "name2", 1748736000000L, 1749504000000L, "country2", "city2", 300, "HUF", "", listOf("tag2"), null, null, true, "user1", ),
                                Travel("3", "name3", 1748736000000L, 1748908800000L, "country3", "city3", 100, "HUF", "", listOf("tag3"), null, null, true, "user2", ))

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
    fun travelUpdated_OnlyTheModifiedDataWillBeSaved(){
        val updatedTravel = travels[0].copy(country="updatedCountry")
        every { travelRepository.findById("1") } returns Optional.of(travels[0])
        every { travelRepository.save(updatedTravel) } returns updatedTravel

        val result = travelService.update("1", TravelUpdateRequest(null, null, null, "updatedCountry", null, null, null, null, null, null))

        verify { travelRepository.save(updatedTravel) }
        assertEquals(updatedTravel, result)
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
        val updatedTravel = Travel("1", "name1", 1748736000000L, 1748908800000L, "country1", "city1", 200, "HUF", "", listOf("tag1"), null, listOf("user2", "user3"), false, "user1", )
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

    @Test
    fun cityFilterTest(){
        val result = travelService.cityFilter("2", travels)

        assertTrue(result.all { it.city!!.contains("2") })
    }

    @Test
    fun countryFilterTest(){
        val result = travelService.countryFilter("2", travels)

        assertTrue(result.all { it.country.contains("2") })
    }

    @Test
    fun nameFilterTest(){
        val result = travelService.nameFilter("2", travels)

        assertTrue(result.all { it.name.contains("2") })
    }

    @Test
    fun minCostFilterTest(){
        val result = travelService.minCostFilter(200, travels)

        assertTrue(result.all { it.price >=200})
    }

    @Test
    fun maxCostFilterTest(){
        val result = travelService.maxCostFiler(200, travels)

        assertTrue(result.all { it.price <=200})
    }

    @Test
    fun tagFilterTest(){
        val result = travelService.tagFilter(listOf("tag1"), travels)

        assertTrue(result.all { it.tags!!.contains("tag1")})
    }

    @Test
    fun minDaysFilterTest(){
        val result = travelService.minDaysFilter(4, travels)

        assertTrue(result.all { travelService.calculateDurationInDays(it.endDate, it.startDate) >=4})
    }

    @Test
    fun maxDaysFilterTest(){
        val result = travelService.maxDaysFilter(6, travels)

        assertTrue(result.all { travelService.calculateDurationInDays(it.endDate, it.startDate) <=6})
    }
}