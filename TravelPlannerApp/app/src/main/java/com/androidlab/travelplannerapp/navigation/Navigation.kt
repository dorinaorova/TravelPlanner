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
import com.androidlab.travelplannerapp.feature.travel.TravelProfileScreen
import com.androidlab.travelplannerapp.feature.vacation.VacationScreen
import com.androidlab.travelplannerapp.feature.registration.RegistrationScreen
import com.androidlab.travelplannerapp.feature.ticket.TicketsScreen
import com.androidlab.travelplannerapp.feature.travel.travelCreate.TravelCreateUpdateScreen
import com.androidlab.travelplannerapp.feature.uploadImage.UploadImageScreen
import com.androidlab.travelplannerapp.feature.userProfile.userUpdate.UserUpdateScreen

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController,
        startDestination = Screen.LoginScreen.route)
    {
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController =navController)
        }
        composable(route = Screen.VacationScreen.route+"?id={id}"){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            VacationScreen(navController = navController, id = id!!)
        }
        composable(route = Screen.SearchScreen.route){
            SearchScreen(navController = navController)
        }
        composable(route = Screen.TravelProfileScreen.route+"?id={id}"){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            TravelProfileScreen(navController = navController, id = id!!)
        }
        composable(route = Screen.PaymentsScreen.route){
            PaymentsScreen(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route+"?id={id}"){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            ProfileScreen(navController = navController, id = id)
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
        composable(route = Screen.UploadImageScreen.route+"?id={id}&uploadImageType={uploadImageType}"){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val uploadImageType = backStackEntry.arguments?.getString("uploadImageType")
            UploadImageScreen(navController = navController, id = id, uploadImageTypeString = uploadImageType!!)
        }
        composable(route = Screen.NewTravelScreen.route+"?id={id}"){backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            TravelCreateUpdateScreen(navController = navController, id = id)
        }
    }
}