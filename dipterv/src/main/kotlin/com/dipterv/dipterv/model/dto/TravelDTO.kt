package com.dipterv.dipterv.model.dto

import com.dipterv.dipterv.model.documentModel.User

data class TravelDTO(
    val _id: String?,
    val travelInfo: TravelInfoDTO,
    val participants: List<User>?,
//        val spends: Array<Spend>,
//        val points: Array<Activity>,
//        val tickets: Array<Ticket>,
    val public: Boolean
)
