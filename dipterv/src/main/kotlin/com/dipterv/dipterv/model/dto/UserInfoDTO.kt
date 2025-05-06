package com.dipterv.dipterv.model.dto


data class UserInfoDTO(
    val _id: String?,
    val username: String,
    val name: String,
    val email: String,
    val description: String?,
    val profilePictureFilePath: String?,
    val backgroundPictureFilePath: String?,
    val travelIds: List<String>?,
    var country: String?,
    var city: String?,
    val followingIds: List<String>?,
    val followerIds: List<String>?,
    val likedTravelIds: List<String>?
    )
