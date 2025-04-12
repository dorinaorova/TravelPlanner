package com.dipterv.dipterv.controller

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.Travel
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
        @RequestParam(required = false) city: String?,
        @RequestParam(required = false) country: String?,
    ) : ResponseEntity<List<Travel>> {
        var travels = travelService.findAllPublicTravels()
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
        city?.let{
            travels = travelService.cityFilter(it, travels)
        }
        country?.let{
            travels = travelService.countryFilter(it, travels)
        }
        return ResponseEntity.ok(travels)
    }

    @GetMapping("/filterValues")
    fun getFilterValues(): ResponseEntity<*>{
        return ResponseEntity.ok(travelService.getFilterValues())
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


    @GetMapping("/participate/{id}")
    fun getParticipatedTravels(@PathVariable("id") id: String) : ResponseEntity<*>{
        try{
            val travels = travelService.findParticipatedTravels(id)
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
            val travel = travelService.findById(id)
            return ResponseEntity.ok(travel)
        }catch (e: NotFoundException){
            return ResponseEntity(e, HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity(e, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/user/{id}")
    fun postNewTravel(@PathVariable("id") userId: String, @RequestBody travel: Travel): ResponseEntity<*> {
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
    fun updateTravel(@RequestBody travel: Travel): ResponseEntity<*> {
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

}