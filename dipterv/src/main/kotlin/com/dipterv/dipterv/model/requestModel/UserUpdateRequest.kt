package com.dipterv.dipterv.model.requestModel

import com.mongodb.client.model.changestream.UpdateDescription

data class UserUpdateRequest(
    val name: String?,
    val city: String?,
    val country: String?,
    val email: String?,
    val description: String?
)
