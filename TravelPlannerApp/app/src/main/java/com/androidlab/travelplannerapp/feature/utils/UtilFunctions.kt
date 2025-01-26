package com.androidlab.travelplannerapp.feature.utils

import android.content.Context
import com.androidlab.travelplannerapp.R

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