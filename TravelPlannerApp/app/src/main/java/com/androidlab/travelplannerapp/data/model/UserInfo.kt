package com.androidlab.travelplannerapp.data.model

data class UserInfo(
    val _id: String?,
    val username: String,
    val name: String,
    val email: String,
    val description: String?,
    val profilePictureFilePath: String?,
    val travelIds: List<String>?
)
