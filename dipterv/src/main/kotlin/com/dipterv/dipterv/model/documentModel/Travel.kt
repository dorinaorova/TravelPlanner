package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "travel")
data class Travel(
        val _id: String?,
        var name: String,
        var startDate: Long,
        var endDate: Long,
        var country: String,
        var city: String?,
        var price: Int,
        var description: String?,
        var tags: List<String>?,
        var pictureFileName: String?,
        var participants: List<User>?,
//        val spends: Array<Spend>,
//        val points: Array<Activity>,
//        val tickets: Array<Ticket>,
        var public: Boolean
)