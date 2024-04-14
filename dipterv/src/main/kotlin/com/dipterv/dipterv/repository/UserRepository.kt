package com.dipterv.dipterv.repository

import com.dipterv.dipterv.model.documentModel.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
}