package com.androidlab.travelplannerapp.data.model

data class Activity(
    val id: String? = null,
    val location: String? = null,
    val name: String,
    val type: ActivityType,
    val travelId: String? = null,
    val visited: Boolean = false
)

enum class ActivityType(val type: String) {
    RESTAURANT("Restaurant"),
    CAFE("Cafe"),
    MUSEUM("Museum"),
    STATUE("Statue"),
    SHOP("Shop"),
    BAR("Bar"),
    OTHER("Other")
}
