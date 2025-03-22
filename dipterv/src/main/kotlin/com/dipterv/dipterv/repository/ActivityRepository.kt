package com.dipterv.dipterv.repository

import com.dipterv.dipterv.model.documentModel.activity.Activity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository : MongoRepository<Activity, String> {
    fun findByTravelId(travelId: String): List<Activity>
}