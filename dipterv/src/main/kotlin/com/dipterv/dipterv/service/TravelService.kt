package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.DTOMapper
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.model.dto.TravelInfoDTO
import com.dipterv.dipterv.repository.TravelRepository
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TravelService (
    val travelRepository: TravelRepository,
    val mapper: DTOMapper,
    val userService: UserService
) {
    fun getById(id: String): Travel{
        try{
            return travelRepository.findById(id).get()
        }catch(e:Exception){
            throw NotFoundException("Travel not found with id: $id")
        }
    }

    fun getAllTravels(): List<Travel> {
        val allTravels = travelRepository.findAll().filter { it.public }
        return allTravels
    }

    fun getTravelInfoById(id: String): TravelInfoDTO{
        return mapper.travelToTravelInfoDto(getById(id))
    }

    fun cityFilter(city: String, travels: List<Travel>) : List<Travel>{
        return travels.filter { it.city!!.contains(city, true) }
    }

    fun countryFilter(country: String, travels: List<Travel>) : List<Travel>{
        return travels.filter { it.country.contains(country, true) }
        return travels.filter { it.country.contains(country, true)}
    }

    fun nameFilter(name: String, travels: List<Travel>): List<Travel>{
        return travels.filter { it.name.contains(name,true) }
    }

    fun minCostFilter(cost: Int, travels: List<Travel>):List<Travel>{
        return travels.filter { it.price>=cost }
    }

    fun maxCostFiler(cost: Int, travels: List<Travel>): List<Travel>{
        return travels.filter { it.price<=cost }
    }

    fun tagFilter(tags: List<String>, travels : List<Travel>): List<Travel>{
        return travels.filter { containsAllTags(it.tags ,tags) }
    }

    fun minDaysFilter(days: Int, travels: List<Travel>) : List<Travel>{
        return travels.filter{ calculateDurationInDays(it.endDate, it.startDate) >= days}
    }

    fun maxDaysFilter(days: Int, travels: List<Travel>) : List<Travel>{
        return travels.filter{ calculateDurationInDays(it.endDate, it.startDate) <= days}
    }

    fun getFilterValues() : List<Int>{
        val travels = getAllTravels()
        val minDaysTravel = travels.minWithOrNull(compareBy {it.endDate - it.startDate})
        val minDays =if(minDaysTravel!= null){calculateDurationInDays(minDaysTravel.endDate, minDaysTravel.startDate)}else{null}
        val maxDaysTravel = travels.maxWithOrNull(compareBy {it.endDate - it.startDate})
        val maxDays =if(maxDaysTravel!= null){calculateDurationInDays(maxDaysTravel.endDate, maxDaysTravel.startDate)}else{null}
        val minPrice = travels.minByOrNull { it.price }?.price
        val maxPrice = travels.maxByOrNull {it.price}?.price
        return listOf(minDays?.toInt()?: 0, maxDays?.toInt()?: 0, minPrice?: 0, maxPrice?:0)
    }

    fun createNew(id: String, travelDTO: Travel): Travel{
        val travel = Travel(
            null,
            travelDTO.name,
            travelDTO.startDate,
            travelDTO.endDate,
            travelDTO.country,
            travelDTO.city,
            travelDTO.price,
            travelDTO.currency,
            travelDTO.description,
            travelDTO.tags,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            travelDTO.public,
            id,

        )
        val newTravel = travelRepository.save(travel)
        userService.addTravel(id, newTravel)
        return newTravel
    }

    fun update(travelRequest: Travel) : Travel{
        try{
            val travel = getById(travelRequest._id!!)//penisz
            travel.name= travelRequest.name
            travel.startDate= travelRequest.startDate
            travel.endDate= travelRequest.endDate
            travel.country= travelRequest.country
            travel.city = travelRequest.city
            travel.price = travelRequest.price
            travel.currency = travelRequest.currency
            travel.description = travelRequest.description
            travel.tags= travelRequest.tags
            travel.public = travelRequest.public
            return travelRepository.save(travelRequest)
        }catch (e: NullPointerException){
            throw NotFoundException("User not found with id: ${travelRequest._id}")
        }
    }

    fun findMyTravels(id: String): List<Travel>? {
        val user = userService.findUserInfoDTOById(id)
        val travels = mutableListOf<Travel>()
        user.travelIds?.forEach { travelId -> travels.add(getById(travelId)) }
        return travels.toList()
    }

    fun findParticipatedTravels(userId: String): List<Travel>?{
        val travels = travelRepository.findAll()
        val partTravels = travels.filter { !it.participantIds.isNullOrEmpty() }
        System.out.println(partTravels)
        val result = partTravels.filter { it.participantIds!!.contains(userId) }
        System.out.println(result)
        return result
    }

    fun addParticipant(userId: String, travelId: String) : Travel{
        try{
            val travel = getById(travelId)
            if(travel.participantIds == null){
                travel.participantIds= emptyList()
            }
            val updatedParticipantIds = travel.participantIds!!.toMutableList().apply { add(userId)}
            travel.participantIds = updatedParticipantIds
            val updatedTravel = travelRepository.save(travel)
            return updatedTravel
        }catch (e: NullPointerException){
            throw NotFoundException("Travel not found with id: ${travelId}")
        }
    }

    fun uploadImage(imageName: String, id: String) : Travel{
        val travel = getById(id)
        travel.pictureFileName=imageName
        return travelRepository.save(travel)
    }

    private fun containsAllTags(base: List<String>?, tags: List<String>) : Boolean{
        if( base === null ) return false
        val baseSet = base.toSet()

        return tags.all{
            baseSet.contains(it)
        }
    }

    private fun calculateDurationInDays(endDate: Long, startDate: Long) : Int{
        val differenceInMillis = endDate - startDate
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis).toInt()
    }

    fun addSpend(spend: Spend, id: String){
        val travel = getById(id)
        if(travel.spendIds == null){
            travel.spendIds = emptyList()
        }
        val updatedSpendList = travel.spendIds!!.toMutableList().apply { add(spend._id) }
        travel.spendIds=updatedSpendList.toList()
        travelRepository.save(travel)
    }

    fun uploadTicket(id: String, ticketId: String){
        val travel = getById(id)
        if(travel.ticketIds == null){
            travel.ticketIds = emptyList()
        }
        val updatedTicketIds = travel.ticketIds!!.toMutableList().apply { add(ticketId)}
        travel.ticketIds=updatedTicketIds.toList()
        travelRepository.save(travel)
    }
}