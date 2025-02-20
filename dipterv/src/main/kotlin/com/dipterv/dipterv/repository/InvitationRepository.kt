package com.dipterv.dipterv.repository

import com.dipterv.dipterv.model.documentModel.TravelInvitation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface InvitationRepository : MongoRepository<TravelInvitation, String> {
    fun findByUserId(userId: String): List<TravelInvitation>
    fun findByTravelId(travelId: String): List<TravelInvitation>
}