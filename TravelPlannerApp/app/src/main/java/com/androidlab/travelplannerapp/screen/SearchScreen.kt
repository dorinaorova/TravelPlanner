package com.androidlab.travelplannerapp.screen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SearchScreen(navController: NavController){

}

@Composable
fun SearchBar(){
    Box(
        modifier= Modifier.background(colorResource(id = R.color.secondary))
    )
}

@Composable
@Preview(showBackground =  true)
fun SearchScreenPreview(){
    SearchScreen(navController = rememberNavController())
}