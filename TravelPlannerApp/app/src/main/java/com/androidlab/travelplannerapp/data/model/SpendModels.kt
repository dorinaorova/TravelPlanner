package com.androidlab.travelplannerapp.data.model

data class Payment (
    val _id: String?,
    val date: Long,
    val userId: String,
    val partUserIds: List<String>,
    val cost: Double,
    val type: SpendType,
    val travelId: String
)

enum class SpendType {
    TRAVEL,
    FOOD,
    MUSEUM,
    OTHER,
    SETTLEMENT
}

data class Transaction(
    val fromUser: String,
    val toUser: String,
    val amount: Double
)