package com.dipterv.dipterv.model.documentModel.activity

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "activity")
data class Activity (
    var id: String?,
    val name: String,
    val type: ActivityType,
    var travelId: String,
    var visited: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
        ) 