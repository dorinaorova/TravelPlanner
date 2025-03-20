package com.dipterv.dipterv.model.documentModel.activity

import com.dipterv.dipterv.model.documentModel.activity.Coordinate

class Activity (
    var id: String?,
    val location: String?,
    val name: String,
    val type: ActivityType,
    var travelId: String,
    var visited: Boolean = false
        )