package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "invitation")
data class TravelInvitation (
    val _id: String?,
    val userId: String,
    val travelId: String,
    var status: InvitationStatus = InvitationStatus.PENDING
)

enum class InvitationStatus{
    ACCEPTED,
    REJECTED,
    PENDING
}