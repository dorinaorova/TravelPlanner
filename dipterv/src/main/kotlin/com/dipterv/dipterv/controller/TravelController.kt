package com.dipterv.dipterv.controller

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Ticket
import com.dipterv.dipterv.model.dto.TravelDTO
import com.dipterv.dipterv.model.dto.TravelInfoDTO
import com.dipterv.dipterv.service.FileService
import com.dipterv.dipterv.service.TicketService
import com.dipterv.dipterv.service.TravelService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths


@RestController
@RequestMapping("/travel")
class TravelController(
    private val travelService: TravelService,
    private val fileService: FileService,
    private val ticketService: TicketService
) {

    @GetMapping("/all")
    fun getAllTravels(
        @RequestParam(required = false) maxPrice: Int?,
        @RequestParam(required = false) minPrice: Int?,
        @RequestParam(required = false) tags: List<String>?,
        @RequestParam(required = false) minDays: Int?,
        @RequestParam(required = false) maxDays: Int?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) location: String?,
    ) : ResponseEntity<List<TravelInfoDTO>> {
        var travels = travelService.getAllTravels()
        maxPrice?.let {
            travels= travelService.maxCostFiler(it, travels)
        }
        minPrice?.let {
            travels = travelService.minCostFilter(it, travels)
        }
        tags?.let {
            travels = travelService.tagFilter(it, travels)
        }
        minDays?.let {
            travels = travelService.minDaysFilter(it, travels)
        }
        maxDays?.let{
            travels = travelService.maxDaysFilter(it, travels)
        }
        name?.let {
            travels= travelService.nameFilter(it, travels)
        }
        location?.let{
            travels = travelService.locationFilter(it, travels)
        }
        return ResponseEntity.ok(travels)
    }

    @GetMapping("/user/{id}")
    fun getMyTravels(@PathVariable("id") id: String) : ResponseEntity<*>{
        try{
            val travels = travelService.findMyTravels(id)
            return ResponseEntity(travels, HttpStatus.OK)
        }catch (e: NotFoundException){
            return ResponseEntity(e, HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity(e, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/{id}")
    fun getTravelById(@PathVariable("id") id: String) : ResponseEntity<*> {
        try {
            val travel = travelService.getTravelInfoById(id)
            return ResponseEntity.ok(travel)
        }catch (e: NotFoundException){
            return ResponseEntity(e, HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity(e, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/user/{id}")
    fun postNewTravel(@PathVariable("id") userId: String, @RequestBody travel: TravelDTO): ResponseEntity<*> {
        try {
            val newTravel = travelService.createNew(userId, travel)
            return ResponseEntity(newTravel, HttpStatus.CREATED)
        }catch (e: NotFoundException){
            return ResponseEntity(e, HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity(e, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/update")
    fun updateTravel(@RequestBody travel: TravelDTO): ResponseEntity<*> {
        try{
            val updatedTravel = travelService.update(travel)
            return ResponseEntity(updatedTravel, HttpStatus.OK)
        }catch (e: NotFoundException){
            return ResponseEntity(e, HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity(e, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/image/upload/{id}")
    fun uploadImage(@RequestPart("file") file: MultipartFile, @PathVariable("id") id: String) : ResponseEntity<*> {
        val fileName = fileService.uploadFile(file, Paths.get("travel/images"), id)
        val updatedTravel = travelService.uploadImage(fileName, id)
        return ResponseEntity.ok(updatedTravel)
    }

    @GetMapping("/image/download/{name}")
    fun downloadImage(@PathVariable("name") name: String) : ResponseEntity<Resource> {
        val image = fileService.downloadFile( Paths.get("travel/images"), name)
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        return ResponseEntity.ok().headers(headers).body(image)
    }

    @PostMapping("/ticket/upload/{id}")
    fun uploadTicket(@RequestPart("file") file: MultipartFile, @RequestBody ticket: Ticket, @PathVariable("id") id: String): ResponseEntity<*>{
        val newTicket = ticketService.saveTicket(ticket, file, id)
        return ResponseEntity(newTicket, HttpStatus.CREATED)
    }

    @GetMapping("/ticket/{id}")
    fun getTicketById(@PathVariable("id") id: String) : ResponseEntity<*> {
        val file = ticketService.findTicketById(id)
        return ResponseEntity(file, HttpStatus.OK)
    }

    @GetMapping("/ticket/travel/{id}")
    fun getTicketByTravelId(@PathVariable("id") id: String) : ResponseEntity<*> {
        val tickets = ticketService.ticketsForTravel(id)
        return ResponseEntity(tickets, HttpStatus.OK)
    }

}