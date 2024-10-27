package com.androidlab.travelplannerapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidlab.travelplannerapp.screen.HomeScreen
import com.androidlab.travelplannerapp.screen.LoginScreen
import com.androidlab.travelplannerapp.screen.PaymentsScreen
import com.androidlab.travelplannerapp.screen.ProfileScreen
import com.androidlab.travelplannerapp.screen.SearchScreen
import com.androidlab.travelplannerapp.screen.TravelProfileScreen
import com.androidlab.travelplannerapp.screen.VacationScreen
import com.androidlab.travelplannerapp.screen.ticket.TicketsScreen

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = Screen.HomeScreen.route)
    {
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController =navController)
        }
        composable(route = Screen.VacationScreen.route){
            VacationScreen(navController = navController)
        }
        composable(route = Screen.SearchScreen.route){
            SearchScreen(navController = navController)
        }
        composable(route = Screen.TravelProfileScreen.route){
            TravelProfileScreen(navController = navController)
        }
        composable(route = Screen.PaymentsScreen.route){
            PaymentsScreen(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route){
            ProfileScreen(navController = navController)
        }
        composable(route= Screen.TicketsScreen.route){
            TicketsScreen(navController = navController)
        }
        composable(route =Screen.LoginScreen.route){
            LoginScreen(navController=navController)
        }
    }
}