package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.spend.Spend
import com.dipterv.dipterv.service.SpendService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/spends")
class SpendsController (val spendService: SpendService) {

    @GetMapping("/travel/{id}")
    fun getSpendsForTravel(@PathVariable id: String): ResponseEntity<List<Spend>>{
        val spends = spendService.findSpendsForTravel(id)
        return ResponseEntity.ok(spends)
    }

    @PostMapping("/travel/{id}")
    fun addSpendForTravel(@PathVariable id: String, @RequestBody spend: Spend) : ResponseEntity<Spend>{
        val newSpend = spendService.addSpend(spend, id)
        return ResponseEntity(newSpend, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteSpend(@PathVariable id: String): ResponseEntity<String>{
        spendService.removeSpend(id)
        return ResponseEntity.ok("deleted")
    }

    @GetMapping("/transactions/{id}")
    fun getTransactions(@PathVariable id: String): ResponseEntity<*>{
        val transactions= spendService.calculateTransactions(id)
        return ResponseEntity.ok(transactions)
    }
}

