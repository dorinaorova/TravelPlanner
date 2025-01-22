package com.androidlab.travelplannerapp.feature.userProfile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {

    fun logout(context: Context, navController: NavController){
        val sharedPreferences = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        navController.navigate(Screen.LoginScreen.route){
            popUpTo(0)
        }
    }
}