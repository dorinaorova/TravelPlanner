package com.androidlab.travelplannerapp.data.model

data class Ticket(
    val _id: String? = null,
    val date: Long,
    val userId: String,
    val travelId: String,
    val name: String,
    val files: List<String>
)
