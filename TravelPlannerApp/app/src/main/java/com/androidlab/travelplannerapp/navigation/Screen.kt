package com.androidlab.travelplannerapp.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object VacationScreen : Screen("vacation")
    object SearchScreen: Screen("search")
}