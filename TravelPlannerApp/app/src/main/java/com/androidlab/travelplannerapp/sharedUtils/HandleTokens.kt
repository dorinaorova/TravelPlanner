package com.androidlab.travelplannerapp.sharedUtils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log

fun getRefreshToken(context: Context): String? {
    val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("refresh_token", null)
    return token
}

fun saveAccessToken(accessToken: String, context: Context) {
    val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("access_token", accessToken).apply()
}