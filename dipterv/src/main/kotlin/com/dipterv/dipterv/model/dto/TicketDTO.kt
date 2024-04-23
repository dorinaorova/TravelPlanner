package com.dipterv.dipterv.model.dto

data class TicketDTO(
    val _id: String,
    val date: Long?,
    val name: String,
    val userName: String?,
    val fileName: String
)
