package com.androidlab.travelplannerapp.data.model

data class Travel(
    val _id: String?,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val country: String,
    val city: String,
    val price: Int,
    val currency: String,
    val description: String?,
    val tags: List<String>?,
    val pictureFileName: String?,
    val public: Boolean,
    val ownerId: String?
)
