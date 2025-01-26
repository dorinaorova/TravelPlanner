package com.androidlab.travelplannerapp.data.model

data class UserInfo(
    val _id: String?,
    val username: String,
    val name: String,
    val email: String,
    val description: String?,
    val profilePictureFilePath: String?,
    val backgroundPictureFilePath: String?,
    val travelIds: List<String>?,
    val city: String?,
    val country: String?,
//    val participatedTravelIds: List<String>?,
    val followingIds: List<String>?,
    val followerIds: List<String>?
)
