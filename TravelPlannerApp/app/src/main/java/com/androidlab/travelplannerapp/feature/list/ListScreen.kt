package com.androidlab.travelplannerapp.feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.feature.utils.TravelListItem
import com.androidlab.travelplannerapp.feature.utils.UserListItem
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun ListScreen(navController: NavController,type: String, userId: String, vm : ListViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.getList(type, userId, context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.primary_background))) {
                CustomList(navController, type)
            }
        },
        bottomBar ={
            NavBar(navController)
        },
        topBar = {
            TopBar(typeMapper(type), navController, Screen.ProfileScreen.route+"?id=${userId}" )
        }
    )
}

private fun typeMapper(type: String):String{
    return when(type){
        "follower" -> "Followers"
        "following" -> "Following"
        "travel" -> "Travels"
        else -> "Liked travels"
    }
}

@Composable
private fun CustomList(navController: NavController,type: String, vm: ListViewModel = hiltViewModel()) {
    val context = LocalContext.current

    if(type == "follower" || type == "following"){
        LazyColumn (){
            items(vm.users){user ->
                UserListItem(navController, user)
            }
        }
    }else{
        LazyColumn (){
            items(vm.travels){travel ->
                TravelListItem(navController, travel, type == "travel", true, vm.likeTravel(travel._id!!, context))
            }
        }

    }
}

