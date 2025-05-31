package com.dipterv.dipterv.model.dto.requestModel

data class TravelUpdateDTO(
    var name: String?,
    var startDate: Long?,
    var endDate: Long?,
    var country: String?,
    var city: String?,
    var price: Int?,
    var currency: String?,
    var description: String?,
    var tags: List<String>?,
    var public: Boolean?,
)