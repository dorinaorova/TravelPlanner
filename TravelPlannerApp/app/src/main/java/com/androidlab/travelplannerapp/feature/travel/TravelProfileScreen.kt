package com.androidlab.travelplannerapp.feature.travel

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.uploadImage.UploadImageType
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.LikeButton
import com.androidlab.travelplannerapp.feature.utils.SmallHeader
import com.androidlab.travelplannerapp.feature.utils.calculateDays
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun TravelProfileScreen(navController: NavController, id: String, vm: TravelViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.fetchData(id, context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
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
        Header()
        Body(scroll, navController)
    }
}

@Composable
private fun Header(vm: TravelViewModel = hiltViewModel()){
    Box(modifier = Modifier
        .fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .blur(5.dp)
        CustomImage(imageModifier, vm.travel.pictureFileName, ImageSourceSelector.TRAVEL, 0.6F)
        Column(Modifier.padding(top = 75.dp)) {
            Text(
                vm.travel.name,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 36.sp,
                modifier = Modifier.padding(start = 25.dp),
                fontFamily = FontFamily(Font(R.font.itim))
            )
            Row(Modifier.padding(horizontal = 25.dp, vertical = 10.dp)) {
                vm.travel.tags?.forEach {
                    TagItem(it)
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
private fun TagItem(text: String){
    Box(Modifier.background(
        MaterialTheme.colorScheme.primaryContainer,
        shape= RoundedCornerShape(10.dp))
        .padding(horizontal=5.dp, vertical=3.dp)
    ){
        Text(text,
            color= MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
@Composable
private fun Body(scroll: ScrollState, navController: NavController, vm: TravelViewModel = hiltViewModel()){
    val context = LocalContext.current
    Column{
        Spacer(
            Modifier
                .height(160.dp)
                .fillMaxWidth())
        Box(Modifier.fillMaxWidth().fillMaxHeight().clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))) {
            Column(
                Modifier
                    .verticalScroll(scroll)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background,
                    )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.padding(start = 25.dp, top = 15.dp, bottom = 15.dp)) {
                        Row(Modifier.clickable { navController.navigate(Screen.ProfileScreen.route + "?id=${vm.user._id}" )}.padding(bottom=10.dp), verticalAlignment = Alignment.CenterVertically){
                            val modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.White, CircleShape)
                            CustomImage(modifier, vm.user.profilePictureFilePath, ImageSourceSelector.PROFILE)
                            Column(Modifier.padding(start=5.dp)){
                                Text(
                                    vm.user.username,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary)
                                Text(vm.user.name, fontSize = 11.sp,modifier=Modifier.padding(start=5.dp), color = MaterialTheme.colorScheme.secondary )
                            }
                        }
                        DataRow(
                            vm.travel.city + ", " + vm.travel.country,
                            ImageVector.vectorResource(R.drawable.baseline_map_24)
                        )
                        DataRow(
                            vm.travel.price.toString() + " " + vm.travel.currency,
                            ImageVector.vectorResource(R.drawable.baseline_attach_money_24)
                        )
                        DataRow(
                            "${
                                calculateDays(
                                    vm.travel.startDate,
                                    vm.travel.endDate
                                )
                            } days (${generateDate(vm.travel.startDate)} - ${generateDate(vm.travel.endDate)})",
                            ImageVector.vectorResource(R.drawable.baseline_calendar_month_24)
                        )
                    }
                    if (!vm.ownTravel(context)) {

                        LikeButton(vm.liked.value,   onClick = {vm.likeTravel(context)})

                    } else {
                        val menuExpanded = remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { menuExpanded.value = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded.value,
                                onDismissRequest = { menuExpanded.value = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("View vacation profile", color = MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.VacationScreen.route + "?id=${vm.travel._id}")
                                    },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                                DropdownMenuItem(
                                    text = { Text("Edit", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.NewTravelScreen.route + "?id=${vm.travel._id}")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_24),
                                            contentDescription = null,
                                            tint= MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                                DropdownMenuItem(
                                    text = { Text("Upload background image", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.UploadImageScreen.route + "?id=${vm.travel._id}&uploadImageType=${UploadImageType.TRAVEL}")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_image_24),
                                            contentDescription = null,
                                            tint= MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                            }
                        }
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp, end = 25.dp, start = 25.dp)
                ) {
                    Text(
                        "Description",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        vm.travel.description ?: "...",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    Modifier.fillMaxWidth()
                        .clickable { navController.navigate(Screen.ActivityListScreen.route + "?id=${vm.travel._id}") }) {
                    SmallHeader("What's the plan?")
                }
                Map(navController)
            }
        }
    }
}

@Composable
private fun DataRow(text: String, icon: ImageVector){
    Row(Modifier.padding(bottom=8.dp),
        verticalAlignment = Alignment.CenterVertically){
        Icon(imageVector = icon,
            contentDescription = text,
            Modifier.padding(end=6.dp),
            tint = MaterialTheme.colorScheme.secondary)
        Text(text, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun Map(navController: NavController, vm: TravelViewModel = hiltViewModel()){
    val context = LocalContext.current

    val boxModifier = Modifier
        .padding(start = 25.dp, end = 25.dp, bottom = 25.dp, top = 10.dp)
        .fillMaxWidth()
        .height(200.dp)
        .border(1.dp, Color.White, RoundedCornerShape(size = 30.dp))
        .clip(RoundedCornerShape(size = 30.dp))
    com.androidlab.travelplannerapp.feature.utils.Map(
        vm.markers,
        { navController.navigate(Screen.MapScreen.route + "?id=${vm.travel._id}") },
        boxModifier,
        vm.mapLoading,
        vm.travel.city, context,
    )
}
