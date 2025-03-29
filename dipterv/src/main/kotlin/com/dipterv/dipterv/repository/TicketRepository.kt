package com.dipterv.dipterv.repository

import com.dipterv.dipterv.model.documentModel.Ticket
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : MongoRepository<Ticket, String> {
    fun findByTravelId(travelId: String): List<Ticket>
}