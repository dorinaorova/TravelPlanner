package com.androidlab.travelplannerapp.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.navigation.Screen
import com.androidlab.travelplannerapp.feature.navbar.NavBar

@Composable
fun HomeScreen(navController: NavController){
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Vacation(navController)
                }
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
fun Vacation(navController: NavController){
    Box(Modifier.background(Color.White)){
        Image(
            painterResource(id = R.drawable.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.7f,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .blur(5.dp),
        )
        Column(modifier = Modifier.padding(start=20.dp)){
            Text("You are on vacation",
                fontSize = 18.sp,
                color = colorResource(id = R.color.secondary_text),
                modifier = Modifier.padding( top=50.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
                Text("Vacation Label",
                    fontSize = 36.sp,
                    color = colorResource(id = R.color.secondary_text),
                    modifier = Modifier.padding(end = 8.dp))
                IconButton(onClick = { navController.navigate(route = Screen.VacationScreen.route)}) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "details",
                        tint = colorResource(id = R.color.secondary_text))
                }
            }

        }
    }

}


@Composable
@Preview(showBackground =  true)
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}