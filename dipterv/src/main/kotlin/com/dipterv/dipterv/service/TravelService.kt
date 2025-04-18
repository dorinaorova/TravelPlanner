package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.requestModel.TravelUpdateRequest
import com.dipterv.dipterv.repository.TravelRepository
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TravelService (
    val travelRepository: TravelRepository,
    val userService: UserService
) {
    fun findById(id: String): Travel{
        try{
            return travelRepository.findById(id).get()
        }catch(e:Exception){
            throw NotFoundException("Travel not found with id: $id")
        }
    }

    fun findAllPublicTravels(): List<Travel> {
        val allTravels = travelRepository.findAll().filter { it.public }
        return allTravels
    }

    fun cityFilter(city: String, travels: List<Travel>) : List<Travel>{
        return travels.filter { it.city!!.contains(city, true) }
    }

    fun countryFilter(country: String, travels: List<Travel>) : List<Travel>{
        return travels.filter { it.country.contains(country, true) }
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
        val travels = findAllPublicTravels()
        val minDaysTravel = travels.minWithOrNull(compareBy { calculateDurationInDays(it.endDate, it.startDate)})
        val minDays =if(minDaysTravel!= null){calculateDurationInDays(minDaysTravel.endDate, minDaysTravel.startDate)}else{null}
        val maxDaysTravel = travels.maxWithOrNull(compareBy {calculateDurationInDays(it.endDate, it.startDate)})
        val maxDays =if(maxDaysTravel!= null){calculateDurationInDays(maxDaysTravel.endDate, maxDaysTravel.startDate)}else{null}
        val minPrice = travels.minByOrNull { it.price }?.price
        val maxPrice = travels.maxByOrNull {it.price}?.price
        return listOf(minDays?: 0, maxDays?: 0, minPrice?: 0, maxPrice?:0)
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
            travelDTO.public,
            id,
        )
        val newTravel = travelRepository.save(travel)
        userService.addTravel(id, newTravel)
        return newTravel
    }

    fun update(id: String, travelRequest: TravelUpdateRequest) : Travel{
        try{
            val travel = findById(id)
            travelRequest.name?.let { travel.name = it }
            travelRequest.startDate?.let { travel.startDate = it }
            travelRequest.endDate?.let { travel.endDate = it }
            travelRequest.country?.let { travel.country = it }
            travelRequest.city?.let { travel.city = it }
            travelRequest.price?.let { travel.price = it }
            travelRequest.currency?.let { travel.currency = it }
            travelRequest.description?.let { travel.description = it }
            travelRequest.tags?.let { travel.tags = it }
            travelRequest.public?.let { travel.public = it }
            return travelRepository.save(travel)
        }catch (e: NullPointerException){
            throw NotFoundException("Travel not found with id: ${id}")
        }
    }

    fun findMyTravels(id: String): List<Travel>? {
        val user = userService.findUserInfoDTOById(id)
        val travels = mutableListOf<Travel>()
        user.travelIds?.forEach { travelId -> travels.add(findById(travelId)) }
        return travels.toList()
    }

    fun findParticipatedTravels(userId: String): List<Travel>?{
        val travels = travelRepository.findAll()
        val partTravels = travels.filter { !it.participantIds.isNullOrEmpty() }
        val result = partTravels.filter { it.participantIds!!.contains(userId) }
        return result
    }

    fun addParticipant(userId: String, travelId: String) : Travel{
        try{
            val travel = findById(travelId)
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
        val travel = findById(id)
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

    fun calculateDurationInDays(endDate: Long, startDate: Long) : Int{
        val differenceInMillis = endDate - startDate
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis).toInt()+1
    }

}