package com.androidlab.travelplannerapp.feature.utils

import android.content.Context
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.ActivityType
import java.text.SimpleDateFormat
import java.util.Date

fun getOwnUserId(context: Context) : String?{
    val sharedPreferences =
        context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
    return sharedPreferences.getString("id", null)
}

fun isFollower(followers: List<String>?, context: Context): Boolean {
    return followers?.find { f -> f == getOwnUserId(context)!! } != null
}

fun ownProfile(id: String?, context: Context): Boolean{
    val savedId = getOwnUserId(context)
    return id == null || id == savedId
}

fun profilePictureFilePath(context: Context, filePath: String): String{
    val BASE_URL = context.getString(R.string.BASE_URL)
    return BASE_URL+"user/image/profile/"+filePath
}

fun backgroundPicturePath(context: Context, filePath: String): String{
    val BASE_URL = context.getString(R.string.BASE_URL)
    return BASE_URL+"user/image/background/"+filePath
}

fun generateDate(date: Long) : String{
    val formatter = SimpleDateFormat("yyyy.MM.dd.")
    return formatter.format(Date(date))
}

fun calculateDays(startDate: Long, endDate: Long): Int{
    return ((endDate - startDate) / (1000 * 60 * 60 * 24)).toInt()
}


fun travelPicturePath(context: Context, fileName: String): String{
    val BASE_URL = context.getString(R.string.BASE_URL)
    return BASE_URL+"travel/image/download/"+fileName
}

fun iconForActivityType(type: ActivityType): Int{
    return when(type){
        ActivityType.RESTAURANT->{
            R.drawable.baseline_restaurant_24
        }
        ActivityType.SHOP->{
            R.drawable.baseline_shopping_basket_24
        }
        ActivityType.MUSEUM->{
            R.drawable.baseline_museum_24
        }
        ActivityType.CAFE -> {
            R.drawable.baseline_local_cafe_24
        }
        ActivityType.BAR -> {
            R.drawable.baseline_local_bar_24
        }
        ActivityType.STATUE -> {
            R.drawable.baseline_add_a_photo_24
        }
        else -> {
            R.drawable.baseline_location_pin_24
        }
    }
}