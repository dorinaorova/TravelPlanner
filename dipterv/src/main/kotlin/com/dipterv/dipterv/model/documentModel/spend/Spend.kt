package com.dipterv.dipterv.model.documentModel.spend

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "spend")
class Spend (
    val _id: String,
    val date: Long,
    val userId: String,
    val cost: Int,
    val type: SpendType
){
}