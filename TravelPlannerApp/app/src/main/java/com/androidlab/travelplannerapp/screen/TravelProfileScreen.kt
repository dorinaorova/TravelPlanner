package com.androidlab.travelplannerapp.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TravelProfileScreen(navController: NavController){

}


@Composable
@Preview(showBackground =  true)
fun TravelProfileScreenPreview(){
    TravelProfileScreen(navController = rememberNavController())
}