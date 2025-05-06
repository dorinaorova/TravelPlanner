package com.androidlab.travelplannerapp.domain.usecases.invitation

import com.androidlab.travelplannerapp.data.service.invitation.InvitationService
import com.androidlab.travelplannerapp.data.model.Invitation
import retrofit2.Call
import javax.inject.Inject

class GetInvitationsByTravelIdUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(id: String): Call<List<Invitation>>? {
        return invitationService.getByTravelId(id)
    }
}

class GetInvitationsByUserIdUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(id: String): Call<List<Invitation>>? {
        return invitationService.getByUserId(id)
    }
}

class CreateInvitationUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(invitation: Invitation): Call<Invitation>? {
        return invitationService.createInvitation(invitation)
    }
}

class DeleteInvitationUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(id: String): Call<Void> {
        return invitationService.delete(id)
    }
}

class AcceptInvitationUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(id: String): Call<Void> {
        return invitationService.accept(id)
    }
}

class RejectInvitationUseCase @Inject constructor(private val invitationService: InvitationService) {
    operator fun invoke(id: String): Call<Void> {
        return invitationService.reject(id)
    }
}