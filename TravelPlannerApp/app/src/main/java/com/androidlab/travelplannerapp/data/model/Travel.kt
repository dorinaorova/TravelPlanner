package com.androidlab.travelplannerapp.data.model

data class Travel(
    val _id: String? = null,
    val name: String ="",
    val startDate: Long =0L,
    val endDate: Long =0L,
    val country: String ="",
    val city: String ="",
    val price: Int =0,
    val currency: String = "EUR",
    val description: String?= null,
    val tags: List<String>?= null,
    val pictureFileName: String?= null,
    val public: Boolean = false,
    val ownerId: String?= null
)
