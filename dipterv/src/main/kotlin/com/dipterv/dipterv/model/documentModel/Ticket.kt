package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ticket")
data class Ticket(
        val _id: String?,
        var date: Long?,
        var userId: String,
        var travelId: String,
        var name: String,
        var files: List<String>
)