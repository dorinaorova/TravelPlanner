package com.dipterv.dipterv.model.dto

import com.dipterv.dipterv.model.documentModel.Travel

data class UserInfoDTO(
    val _id: String?,
    val username: String,
    val name: String,
    val email: String,
    val description: String?,
    val profilePictureFilePath: String?,
    val travelIds: List<String>?
    )
