package com.dipterv.dipterv.model.dto

data class TravelDTO(
    val _id: String?,
    val travelInfo: TravelInfoDTO,
    val participantIds: List<String>?,
    val spendIds: List<String>?,
//        val points: Array<Activity>,
    val ticketIds: List<String>?,
    val public: Boolean
)
