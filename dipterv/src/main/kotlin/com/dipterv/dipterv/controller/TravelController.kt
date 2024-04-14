package com.dipterv.dipterv.controller

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Travel
import com.dipterv.dipterv.model.dto.TravelDTO
import com.dipterv.dipterv.model.dto.TravelInfoDTO
import com.dipterv.dipterv.repository.TravelRepository
import com.dipterv.dipterv.service.TravelService
import com.dipterv.dipterv.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/travel")
class TravelController(
    private val travelService: TravelService,
    private val userService: UserService
) {

    @GetMapping("/all")
    fun getAllTravels(
        @RequestParam(required = false) maxPrice: Int?,
        @RequestParam(required = false) minPrice: Int?,
        @RequestParam(required = false) tags: List<String>?,
        @RequestParam(required = false) days: String?,
        @RequestParam(required = false) name: String?,
    ) : ResponseEntity<List<TravelInfoDTO>> {
        var travels = travelService.getAllTravels()
        maxPrice?.let {
            travels= travelService.maxCostFiler(maxPrice, travels)
        }
        minPrice?.let {
            travels = travelService.minCostFilter(minPrice, travels)
        }
        tags?.let {
            travels = travelService.tagFilter(tags, travels)
        }
        days?.let {

        }
        name?.let {
            travels= travelService.nameFilter(name, travels)
        }
        return ResponseEntity.ok(travels)
    }

    @GetMapping("/user/{id}")
    fun getMyTravels(@RequestParam("id") userId: Long){

    }

    @GetMapping("/{id}")
    fun getTravelById(@PathVariable("id") id: String) : ResponseEntity<TravelInfoDTO> {
        try {
            val travel = travelService.getTravelInfoById(id)
            return ResponseEntity.ok(travel)
        }catch (e: NotFoundException){
            return ResponseEntity.notFound().build()
        }catch (e: Exception){
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/user/{id}")
    fun postNewTravel(@PathVariable("id") userId: Long, @RequestBody travel: TravelDTO): ResponseEntity<TravelDTO> {
        val newTravel= travelService.createNew(travel)
        return ResponseEntity(newTravel, HttpStatus.CREATED)
    }
}