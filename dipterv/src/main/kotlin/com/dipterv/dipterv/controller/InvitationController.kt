package com.dipterv.dipterv.controller

import com.dipterv.dipterv.model.documentModel.TravelInvitation
import com.dipterv.dipterv.service.InvitationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/invitation")
class InvitationController (private val invitationService: InvitationService) {

    @GetMapping("/user/{id}")
    fun getInvitationsByUserId(@PathVariable("id") id: String) : ResponseEntity<List<TravelInvitation>> {
        return ResponseEntity.ok(invitationService.findByUserId(id))
    }

    @GetMapping("/travel/{id}")
    fun getInvitationsByTravelId(@PathVariable("id") id: String) : ResponseEntity<List<TravelInvitation>> {
        return ResponseEntity.ok(invitationService.findByTravelId(id))
    }

    @PostMapping()
    fun createInvitation(@RequestBody invitation: TravelInvitation) : ResponseEntity<*>{
        val newInvitation = invitationService.createInvitation(invitation)
        return ResponseEntity(newInvitation, HttpStatus.CREATED)
    }

    @GetMapping("/accept/{id}")
    fun acceptInvitation(@PathVariable("id") id: String){
        val invitation = invitationService.acceptInvitation(id)
    }

    @GetMapping("/reject/{id}")
    fun declineInvitation(@PathVariable("id") id: String){
        val invitation = invitationService.declineInvitation(id)
    }

    @DeleteMapping("{id}")
    fun deleteInvitation(@PathVariable("id") id: String){
        invitationService.deleteInvitation(id)
    }
}