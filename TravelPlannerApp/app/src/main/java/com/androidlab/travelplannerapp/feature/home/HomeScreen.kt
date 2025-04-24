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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.ListItemDivider
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen


@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.fetchTravels(context)
        vm.fetchInvitations(context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(MaterialTheme.colorScheme.background)) {
                Column{
                    CurrentVacation(navController)
                    Invitations(navController)
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
        color = MaterialTheme.colorScheme.primary,
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
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "${ generateDate(travel.startDate) } - ${ generateDate(travel.endDate) }",
                                    fontSize = 12.sp,
                            modifier = Modifier.padding(start=8.dp),
                            color = MaterialTheme.colorScheme.primary)
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
            CustomImage(imageModifier, currentVacation.pictureFileName, ImageSourceSelector.TRAVEL, 0.6F)
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    "You are on vacation",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 50.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp).clickable {navController.navigate(route = Screen.VacationScreen.route+"?id=${currentVacation._id}")  }
                ) {
                    Text(
                        currentVacation.name,
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "details",
                            tint = MaterialTheme.colorScheme.primary
                        )

                }

            }
        }
    }

}

@Composable
fun Invitations(navController: NavController, vm: HomeViewModel = hiltViewModel()){
  if(vm.invitations.isNotEmpty()){
      val context = LocalContext.current
      Column (Modifier.fillMaxWidth().heightIn(0.dp, 250.dp)){
          Text("Invitations", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, modifier =Modifier.padding(horizontal=15.dp, vertical=10.dp))
          LazyColumn {
              items(vm.invitations) { inv ->
                  Row(
                      Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 3.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                      Text(
                          vm.findTravelNameById(inv.travelId) ?: "Not found",
                          fontSize = 16.sp,
                          color = MaterialTheme.colorScheme.primary,
                          modifier = Modifier.clickable(onClick = { navController.navigate(Screen.TravelProfileScreen.route + "?id=${inv.travelId}") })
                      )

                      Row(verticalAlignment = Alignment.CenterVertically) {
                          TextButton(onClick = { vm.answerInvitation(inv._id!!, true, context)}) {
                              Text("Accept", color = MaterialTheme.colorScheme.primary) }
                          Text("/", color= MaterialTheme.colorScheme.primary)
                          TextButton(onClick = { vm.answerInvitation(inv._id!!, false, context)}) {
                              Text("Decline", color = MaterialTheme.colorScheme.primary) }
                      }
                  }
                  Divider(
                      thickness = 1.dp,
                      color = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.padding(horizontal = 25.dp, vertical = 3.dp)
                  )
              }
          }
          Divider(
              thickness = 2.dp,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
  }
}
}