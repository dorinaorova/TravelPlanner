package com.androidlab.travelplannerapp.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object VacationScreen : Screen("vacation")
    object SearchScreen: Screen("search")
    object TravelProfileScreen: Screen ("travel_profile")
    object PaymentsScreen: Screen("payments")
    object ProfileScreen: Screen("profile")
    object TicketsScreen: Screen("tickets")
}