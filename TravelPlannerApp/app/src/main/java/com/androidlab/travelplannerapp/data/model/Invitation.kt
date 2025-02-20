package com.androidlab.travelplannerapp.data.model

data class Invitation(
    val _id: String?,
    val userId: String,
    val travelId: String,
    val status: Status = Status.PENDING,
)

enum class Status {
    PENDING,
    ACCEPTED,
    REJECTED
}