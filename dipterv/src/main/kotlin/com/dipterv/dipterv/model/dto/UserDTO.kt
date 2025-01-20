package com.dipterv.dipterv.model.dto

import com.dipterv.dipterv.model.documentModel.User

data class UserDTO(
    val _id: String?,
    val userInfo: UserInfoDTO,
    val followingIds: List<String>?,
    val followerIds: List<String>?,
)
