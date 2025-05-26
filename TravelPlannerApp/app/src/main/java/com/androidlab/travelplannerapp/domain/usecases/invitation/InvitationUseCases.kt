package com.androidlab.travelplannerapp.domain.usecases.invitation

import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.repository.InvitationRepository
import retrofit2.Call
import javax.inject.Inject

class GetInvitationsByTravelIdUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(id: String): Call<List<Invitation>>? {
        return invitationRepository.getByTravelId(id)
    }
}

class GetInvitationsByUserIdUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(id: String): Call<List<Invitation>>? {
        return invitationRepository.getByUserId(id)
    }
}

class CreateInvitationUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(invitation: Invitation): Call<Invitation>? {
        return invitationRepository.createInvitation(invitation)
    }
}

class DeleteInvitationUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(id: String): Call<Void> {
        return invitationRepository.delete(id)
    }
}

class AcceptInvitationUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(id: String): Call<Void> {
        return invitationRepository.accept(id)
    }
}

class RejectInvitationUseCase @Inject constructor(private val invitationRepository: InvitationRepository) {
    operator fun invoke(id: String): Call<Void> {
        return invitationRepository.reject(id)
    }
}