package com.androidlab.travelplannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.navigation.Navigation
import com.example.compose.TravelPlannerAppTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController : NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelPlannerAppTheme {
                navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}