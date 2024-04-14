package com.dipterv.dipterv.model.documentModel

import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "users")
data class User(
        val _id: String?,
        var username: String,
        val name: String,
        var email: String,
        var description: String?,
        val profilePictureFilePath: String?,
        val travels: List<Travel>,
        var following: List<User>,
        val followers: List<User>,
)