package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "users")
data class User(
        val _id: String?,
        val username: String,
        val password: String,
        var name: String,
        var email: String,
        var description: String?,
        val profilePictureFilePath: String?,
        val travelIds: List<String>,
        val participatedTravelIds: List<String>,
        var followingIds: List<String>,
        val followerIds: List<String>,
)