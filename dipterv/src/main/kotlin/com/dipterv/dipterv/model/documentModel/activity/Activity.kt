package com.dipterv.dipterv.model.documentModel.activity

import com.dipterv.dipterv.model.documentModel.activity.Coordinate

class Activity (
    val id: String,
    val location: Coordinate,
    val name: String,
    val type: ActivityType
        ){
}