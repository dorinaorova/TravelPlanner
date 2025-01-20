package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ticket")
data class Ticket(
        val _id: String,
        var date: Long?,
        var name: String,
        var userName: String?,
        var fileName: String
)