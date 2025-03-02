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
        var currency: String,
        var description: String?,
        var tags: List<String>?,
        var pictureFileName: String?,
        var participantIds: List<String>?,
//        val points: Array<Activity>,
        var public: Boolean,
        var ownerId: String?,
        var isCurrent: Boolean = false
)