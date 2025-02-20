package com.dipterv.dipterv.service

import com.dipterv.dipterv.exception.NotFoundException
import com.dipterv.dipterv.model.documentModel.InvitationStatus
import com.dipterv.dipterv.model.documentModel.TravelInvitation
import com.dipterv.dipterv.repository.InvitationRepository
import org.springframework.stereotype.Service

@Service
class InvitationService(private val invitationRepository: InvitationRepository, private val travelService: TravelService) {
    fun createInvitation(invitation: TravelInvitation): TravelInvitation{
        invitation.status = InvitationStatus.PENDING
        return invitationRepository.save(invitation)
    }

    fun findByUserId(userId: String): List<TravelInvitation>{
        return invitationRepository.findByUserId(userId)
    }

    fun acceptInvitation(id: String): TravelInvitation{
        try{
            val invitation = invitationRepository.findById(id).get()
            invitation.status = InvitationStatus.ACCEPTED
            travelService.addParticipant(invitation.userId, invitation.travelId)
            return invitationRepository.save(invitation)
        }catch(e: Exception){
            throw NotFoundException("Invitation not found with id: $id")
        }
    }
    fun findByTravelId(id: String): List<TravelInvitation>{
        return invitationRepository.findByTravelId(id)
    }

    fun declineInvitation(id: String) : TravelInvitation{
        try{
            val invitation = invitationRepository.findById(id).get()
            invitation.status = InvitationStatus.REJECTED
            return invitationRepository.save(invitation)
        }catch(e: Exception){
            throw NotFoundException("Invitation not found with id: $id")
        }
    }

    fun deleteInvitation(id: String){
        try{
            invitationRepository.deleteById(id)
        }catch(e: Exception){
            throw NotFoundException("Invitation not found with id: $id")
        }
    }
}