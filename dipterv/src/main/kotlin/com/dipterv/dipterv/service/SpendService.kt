package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.repository.SpendRepository
import org.springframework.stereotype.Service

@Service
class SpendService (val spendRepository: SpendRepository, val travelService:TravelService) {

    fun findById(id: String): Spend {
        try {
            val spend = spendRepository.findById(id).get()
            return spend
        }catch (ex: Exception){
            throw NotFoundException("Spend with id $id not found")
        }
    }

    fun addSpend(spend: Spend, travelId: String) : Spend{
        val savedSpend = spendRepository.save(spend)
        travelService.addSpend(savedSpend, travelId)
        return savedSpend
    }

    fun removeSpend(id: String){
        val spend = findById(id)
        spendRepository.delete(spend)
    }

    fun findSpendsForTravel(travelId: String) : List<Spend> {
        val spendIds = travelService.getById(travelId).spendIds
        val spends = mutableListOf<Spend>()
        spendIds?.forEach { spendId -> spends.add(findById(spendId)) }
        return spends.toList()
    }
}