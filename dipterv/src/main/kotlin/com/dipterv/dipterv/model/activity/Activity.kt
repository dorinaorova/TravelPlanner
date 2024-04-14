package com.dipterv.dipterv.model.activity

import com.dipterv.dipterv.model.activity.Coordinate

class Activity (
        val id: Long,
        val location: Coordinate,
        val name: String,
        val type: ActivityType
        ){
}