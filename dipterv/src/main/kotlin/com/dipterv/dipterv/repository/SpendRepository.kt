package com.dipterv.dipterv.repository
import com.dipterv.dipterv.model.documentModel.spend.Spend
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SpendRepository  : MongoRepository<Spend, String> {
}