package com.androidlab.travelplannerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.androidlab.travelplannerapp.feature.HomeScreen
import com.androidlab.travelplannerapp.feature.login.LoginScreen
import com.androidlab.travelplannerapp.feature.PaymentsScreen
import com.androidlab.travelplannerapp.feature.userProfile.ProfileScreen
import com.androidlab.travelplannerapp.feature.search.SearchScreen
import com.androidlab.travelplannerapp.feature.TravelProfileScreen
import com.androidlab.travelplannerapp.feature.VacationScreen
import com.androidlab.travelplannerapp.feature.registration.RegistrationScreen
import com.androidlab.travelplannerapp.feature.ticket.TicketsScreen
import com.androidlab.travelplannerapp.feature.userProfile.userUpdate.UserUpdateScreen

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = Screen.LoginScreen.route)
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
        composable(route=Screen.RegistrationScreen.route){
            RegistrationScreen(navController = navController)
        }
        composable(route=Screen.UserUpdateScreen.route){
            UserUpdateScreen(navController = navController)
        }
    }
}