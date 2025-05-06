package com.androidlab.travelplannerapp.feature.vacation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.Map
import com.androidlab.travelplannerapp.feature.utils.SmallHeader
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun VacationScreen(navController: NavController, id: String, vm: VacationViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.fetchData(id, context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).testTag("vacationScreen")) {
                Details(navController)
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
private fun Details(navController: NavController){
    Box(Modifier.fillMaxSize()){
        val scroll = rememberScrollState(0)
        Header(navController)
        Body(scroll, navController)
    }
}

@Composable
private fun Header(navController: NavController, vm: VacationViewModel = hiltViewModel()){
    Box(modifier = Modifier
        .fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val imageModifier =  Modifier
            .fillMaxWidth()
            .height(300.dp)
            .blur(5.dp)
        CustomImage(imageModifier, vm.travel.pictureFileName, ImageSourceSelector.TRAVEL, 0.6F)
        Column(Modifier.padding(top = 75.dp).clickable { navController.navigate(Screen.TravelProfileScreen.route) }) {
            Text(
                vm.travel.name,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 36.sp,
                modifier = Modifier.padding(start = 25.dp).testTag("vacationName"),
                fontFamily = FontFamily(Font(R.font.itim))
            )
        }
    }
}

@Composable
private fun Body(scroll: ScrollState, navController: NavController, vm: VacationViewModel = hiltViewModel()){
    Column{
        Spacer(
            Modifier
                .height(160.dp)
                .fillMaxWidth())
        Box(Modifier.fillMaxWidth().fillMaxHeight().clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))){
            Column(
                Modifier
                    .verticalScroll(scroll)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background,
                    )){
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    val menuExpanded = remember { mutableStateOf(false) }
                    Box{
                        IconButton(onClick = { menuExpanded.value= true }) {
                            Icon(
                                imageVector= Icons.Rounded.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                        ){
                            DropdownMenuItem(
                                text={ Text("View travel profile", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                onClick = {
                                    menuExpanded.value = false
                                    navController.navigate(Screen.TravelProfileScreen.route+"?id=${vm.travel._id}")
                                },
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            )

                        }
                    }
                }
                TravelBuddies(navController)
                Payments(navController)
                Plan(navController)
                Tickets(navController )
            }
        }
    }
}

@Composable
private fun TravelBuddies(navController: NavController, vm: VacationViewModel = hiltViewModel()){
    SmallHeader("Your travel buddies")
    if(vm.participants.isNotEmpty()){
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(end = 25.dp, start = 30.dp)){
            items(vm.participants) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp)
                            .clickable { navController.navigate(Screen.ProfileScreen.route+"?id=${it._id}") }.testTag("vacation_participant_${it._id}")) {
                    Box(Modifier.height(50.dp).width(50.dp).clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape).padding(bottom = 3.dp)){
                        val imageModifier = Modifier
                            .fillMaxSize()
                        CustomImage(imageModifier, it.profilePictureFilePath, ImageSourceSelector.PROFILE)
                    }

                    Text(it.username,color= MaterialTheme.colorScheme.primary)
                    if(it._id == vm.travel.ownerId) Text("(Owner)", color = MaterialTheme.colorScheme.secondary, fontSize = 10.sp)
                }
            }
        }
    }
    TextButton(onClick = { navController.navigate(Screen.InvitationScreen.route+"?id=${vm.travel._id}")}, modifier = Modifier.testTag("addMember")) {
        Text("Add new member",
            color= MaterialTheme.colorScheme.secondary,
            modifier=Modifier.padding(end=25.dp, start=25.dp))
    }
}

@Composable
private fun Payments(navController: NavController, vm: VacationViewModel = hiltViewModel()){
    Column(Modifier.clickable { navController.navigate(Screen.PaymentsScreen.route+"?id=${vm.travel._id}")  }.testTag("payments")){
        SmallHeader("Payments")
        if(vm.ownTransaction.isNotEmpty()){
            val transaction = vm.ownTransaction[0]
            val context= LocalContext.current
            Row(verticalAlignment = Alignment.Top,
                modifier=Modifier.padding(horizontal=30.dp).testTag("transaction")){
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(vm.findUserName(transaction.fromUser, context),
                        fontSize=14.sp, color= MaterialTheme.colorScheme.secondary, modifier = Modifier.testTag("transaction_fromUser"))
                    Text(transaction.amount.toString(),
                        color= MaterialTheme.colorScheme.primary,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.testTag("transaction_amount"))
                }
                Icon(imageVector = ImageVector.vectorResource(R.drawable.arrow_forward),
                    contentDescription = null,
                    tint= MaterialTheme.colorScheme.secondary,
                    modifier= Modifier
                        .height(30.dp)
                        .width(30.dp))
                Text(vm.findUserName(transaction.toUser, context),
                    fontSize=14.sp, color= MaterialTheme.colorScheme.primary, modifier = Modifier.testTag("transaction_toUser"))
            }
        }
        Row(Modifier.padding(horizontal=30.dp)){
            Text("You are in: ", color= MaterialTheme.colorScheme.secondary)
            Text(vm.ownDebt.toString(),
                color= MaterialTheme.colorScheme.primary, modifier = Modifier.testTag("ownDept"))
        }
    }
}

@Composable
private fun Plan(navController: NavController, vm: VacationViewModel = hiltViewModel() ){
    Column{
        Row(Modifier.fillMaxWidth().clickable { navController.navigate(Screen.ActivityListScreen.route+"?id=${vm.travel._id}") }.testTag("plan")){
            SmallHeader("What's the plan?")
        }
        val boxModifier = Modifier
            .padding(start = 25.dp, end = 25.dp, bottom = 25.dp, top = 10.dp)
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.White, RoundedCornerShape(size = 30.dp))
            .clip(RoundedCornerShape(size = 30.dp))
            Map(vm.markers, {navController.navigate(Screen.MapScreen.route+"?id=${vm.travel._id}") }, boxModifier, vm.mapLoading, vm.travel.city, LocalContext.current)
    }
}

@Composable
private fun Tickets(navController: NavController, vm: VacationViewModel = hiltViewModel()){
    Box(Modifier.clickable { navController.navigate(Screen.TicketsScreen.route+"?id=${vm.travel._id}") }.testTag("tickets")) {
        SmallHeader("Tickets")
    }
            if(vm.tickets.isEmpty()){
                    Text("Add new ticket",
                        color=MaterialTheme.colorScheme.secondary,
                        modifier=Modifier.padding(end=25.dp, start=25.dp, bottom=25.dp))
            }
            else{
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 25.dp, start = 30.dp, bottom = 30.dp)){
                    items(vm.tickets) {
                        Box(modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(80.dp, 60.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(15.dp)
                            )) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()){
                                Text(it.name,
                                    fontSize=14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(
                                    generateDate(it.date),
                                    fontSize=10.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    }
                }
            }

}