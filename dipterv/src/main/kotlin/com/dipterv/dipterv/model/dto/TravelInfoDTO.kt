package com.dipterv.dipterv.model.dto

data class TravelInfoDTO(
    val _id: String?,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val country: String,
    val city: String?,
    val price: Int,
    val description: String?,
    val tags: List<String>?,
    val pictureFileName: String?
)
