package com.androidlab.travelplannerapp.navigation

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login")
    object HomeScreen : Screen("home")
    object VacationScreen : Screen("vacation")
    object SearchScreen: Screen("search")
    object TravelProfileScreen: Screen ("travel_profile")
    object PaymentsScreen: Screen("payments")
    object ProfileScreen: Screen("profile")
    object TicketsScreen: Screen("tickets")
    object RegistrationScreen: Screen("registration")
    object UserUpdateScreen: Screen("user_update")
    object UploadImageScreen: Screen("upload_image")
    object NewTravelScreen: Screen("new_travel")
    object InvitationScreen: Screen("invitation")
    object ActivityListScreen: Screen("activity_list")
    object MapScreen: Screen("map")
    object ListScreen: Screen("list")
}