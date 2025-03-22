package com.dipterv.dipterv.model.documentModel.activity

class Activity (
    var id: String?,
    val name: String,
    val type: ActivityType,
    var travelId: String,
    var visited: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
        ) 