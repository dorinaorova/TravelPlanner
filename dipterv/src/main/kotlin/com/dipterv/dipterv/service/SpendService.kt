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

    fun calculateDebt(travelId: String): Map<String, Double>{
        val spends = findSpendsForTravel(travelId)
        val userIds = spends.flatMap { it.partUserIds.asList() }.toSet()
        val debts = mutableMapOf<String, Double>()
        userIds.forEach{id->
            var debt =0.0
            spends.forEach{spend ->
                if(spend.userId == id){
                    debt+=spend.cost
                }
                if(spend.partUserIds.contains(id)){
                    debt-=spend.cost/spend.partUserIds.size
                }
            }
            debts.put(id, debt)
        }

        return debts
    }

    fun calculateTransactions(travelId: String) : List<Transaction>{
        val debts = calculateDebt(travelId)
        val transactions = mutableListOf<Transaction>()

        val debtors = debts.filter { it.value < 0 }.map { it.key to -it.value }.toMutableList()
        val creditors = debts.filter { it.value > 0 }.map { it.key to it.value }.toMutableList()

        var debtorIndex = 0
        var creditorIndex = 0

        while (debtorIndex < debtors.size && creditorIndex < creditors.size) {
            val (debtor, debtAmount) = debtors[debtorIndex]
            val (creditor, creditAmount) = creditors[creditorIndex]

            val settledAmount = minOf(debtAmount, creditAmount)

            transactions.add(Transaction(debtor, creditor, settledAmount))

            debtors[debtorIndex] = debtor to (debtAmount - settledAmount)
            creditors[creditorIndex] = creditor to (creditAmount - settledAmount)

            if (debtors[debtorIndex].second == 0.0) debtorIndex++
            if (creditors[creditorIndex].second == 0.0) creditorIndex++
        }

        return transactions

    }
}

data class Transaction(val fromUser: String, val toUser: String, val amount: Double)