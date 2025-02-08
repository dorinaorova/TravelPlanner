package com.androidlab.travelplannerapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.ListItemDivider
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen
import com.example.compose.primaryBackgroundCustom
import com.example.compose.primaryCustom

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.fetchTravels(context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(primaryBackgroundCustom)) {
                Column{
                    CurrentVacation(navController)
                    MyVacationList(navController)
                }
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}
@Composable
fun MyVacationList(navController: NavController, vm: HomeViewModel = hiltViewModel()){
    Text(
        "Upcoming vacations",
        fontSize = 24.sp,
        color = primaryCustom,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
    )
    LazyColumn {
        items(vm.filterUpcomingTravels()){travel->
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Row(Modifier.clickable { navController.navigate(Screen.VacationScreen.route+"?id=${travel._id}") }) {
                    Box(
                        Modifier
                            .width(90.dp)
                            .height(80.dp)
                            .padding(horizontal = 10.dp)
                    ) {
                        val imageModifier = Modifier.fillMaxSize()
                        CustomImage(imageModifier, travel.pictureFileName, ImageSourceSelector.TRAVEL)
                    }
                    Column {
                        Text(
                            travel.name,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            "${ generateDate(travel.startDate) } - ${ generateDate(travel.endDate) }",
                                    fontSize = 12.sp,
                            modifier = Modifier.padding(start=8.dp),
                            color = Color.Black)
                    }
                }
            }
            ListItemDivider()
        }
    }
}

@Composable
fun CurrentVacation(navController: NavController, vm: HomeViewModel = hiltViewModel()){
    val currentVacation = vm.getCurrentVacation()
    if(currentVacation != null) {
        Box(Modifier.height(300.dp).fillMaxWidth()) {
            val imageModifier = Modifier
                .fillMaxSize()
                .blur(5.dp)
            CustomImage(imageModifier, currentVacation.pictureFileName, ImageSourceSelector.TRAVEL)
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    "You are on vacation",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.secondary_text),
                    modifier = Modifier.padding(top = 50.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
                ) {
                    Text(
                        currentVacation.name,
                        fontSize = 36.sp,
                        color = colorResource(id = R.color.secondary_text),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(onClick = { navController.navigate(route = Screen.VacationScreen.route+"?id=${currentVacation._id}") }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "details",
                            tint = colorResource(id = R.color.secondary_text)
                        )
                    }
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