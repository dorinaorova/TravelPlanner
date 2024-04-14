package com.dipterv.dipterv.repository

import com.dipterv.dipterv.model.documentModel.Travel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TravelRepository : MongoRepository<Travel, String> {
}