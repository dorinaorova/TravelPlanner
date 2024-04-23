package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.DTOMapper
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.model.dto.TravelDTO
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
            throw NotFoundException("User not found with id: $id")
        }
    }

    fun getAllTravels(): List<TravelInfoDTO> {
        val allTravels = travelRepository.findAll().filter { it.public }
        return allTravels.map { travel-> mapper.travelToTravelInfoDto(travel) }
    }

    fun getTravelInfoById(id: String): TravelInfoDTO{
        return mapper.travelToTravelInfoDto(getById(id))
    }

    fun locationFilter(location: String, travels: List<TravelInfoDTO>) : List<TravelInfoDTO>{
        return travels.filter { it.country.contains(location, true) || it.city!!.contains(location, true) }
    }

    fun nameFilter(name: String, travels: List<TravelInfoDTO>): List<TravelInfoDTO>{
        return travels.filter { it.name.contains(name,true) }
    }

    fun minCostFilter(cost: Int, travels: List<TravelInfoDTO>):List<TravelInfoDTO>{
        return travels.filter { it.price>=cost }
    }

    fun maxCostFiler(cost: Int, travels: List<TravelInfoDTO>): List<TravelInfoDTO>{
        return travels.filter { it.price<=cost }
    }

    fun tagFilter(tags: List<String>, travels : List<TravelInfoDTO>): List<TravelInfoDTO>{
        return travels.filter { containsAllTags(it.tags ,tags) }
    }

    fun minDaysFilter(days: Int, travels: List<TravelInfoDTO>) : List<TravelInfoDTO>{
        return travels.filter{ calculateDurationInDays(it.endDate, it.startDate) >= days}
    }

    fun maxDaysFilter(days: Int, travels: List<TravelInfoDTO>) : List<TravelInfoDTO>{
        return travels.filter{ calculateDurationInDays(it.endDate, it.startDate) <= days}
    }

    fun createNew(id: String, travelDTO: TravelDTO): TravelDTO{
        val travel = Travel(
            null,
            travelDTO.travelInfo.name,
            travelDTO.travelInfo.startDate,
            travelDTO.travelInfo.endDate,
            travelDTO.travelInfo.country,
            travelDTO.travelInfo.city,
            travelDTO.travelInfo.price,
            travelDTO.travelInfo.description,
            travelDTO.travelInfo.tags,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            travelDTO.public,
            id
        )
        val newTravel = travelRepository.save(travel)
        userService.addTravel(id, newTravel)
        return mapper.travelToTravelDTO(travel)
    }

    fun update(travelDTO: TravelDTO) : TravelDTO{
        try{
            val travel = getById(travelDTO._id!!)
            travel.name= travelDTO.travelInfo.name
            travel.startDate= travelDTO.travelInfo.startDate
            travel.endDate= travelDTO.travelInfo.endDate
            travel.country= travelDTO.travelInfo.country
            travel.city = travelDTO.travelInfo.city
            travel.price = travelDTO.travelInfo.price
            travel.description = travelDTO.travelInfo.description
            travel.tags= travelDTO.travelInfo.tags
            val updatedTravel = travelRepository.save(travel)
            return mapper.travelToTravelDTO(updatedTravel)
        }catch (e: NullPointerException){
            throw NotFoundException("User not found with id: ${travelDTO._id}")
        }
    }

    fun findMyTravels(id: String): List<TravelDTO>? {
        val user = userService.findUserInfoDTOById(id)
        val travels = mutableListOf<TravelDTO>()
        user.travelIds?.forEach { travelId -> travels.add(mapper.travelToTravelDTO(getById(travelId))) }
        return travels.toList()
    }

//    fun updateParticipant(travelDTO: TravelDTO) : TravelDTO{
//        try{
//            val travel = getById(travelDTO._id!!)
//            travel.participants = travelDTO.participants
//            val updatedTravel = travelRepository.save(travel)
//            return TravelDTO(
//                updatedTravel._id,
//                travelToTravelInfoDto(updatedTravel),
//                updatedTravel.participants,
//                updatedTravel.public
//            )
//        }catch (e: NullPointerException){
//            throw NotFoundException("User not found with id: ${travelDTO._id}")
//        }
//    }

    fun uploadImage(imageName: String, id: String) : TravelInfoDTO{
        val travel = getById(id)
        travel.pictureFileName=imageName
        val updatedTravel = travelRepository.save(travel)
        return mapper.travelToTravelInfoDto(updatedTravel)
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
        val updatedSpendList = travel.spendIds.toMutableList().apply { add(spend._id) }
        travel.spendIds=updatedSpendList.toList()
        travelRepository.save(travel)
    }

    fun uploadTicket(id: String, ticketId: String){
        val travel = getById(id)
        val updatedTicketIds = travel.ticketIds.toMutableList().apply { add(ticketId)}
        travel.ticketIds=updatedTicketIds.toList()
        travelRepository.save(travel)
    }
}