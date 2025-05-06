package com.androidlab.travelplannerapp.feature.userProfile

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.uploadImage.UploadImageType
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.LikeButton
import com.androidlab.travelplannerapp.feature.utils.ListItemDivider
import com.androidlab.travelplannerapp.feature.utils.isFollower
import com.androidlab.travelplannerapp.feature.utils.ownProfile
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController, id: String?, vm: UserProfileViewModel = hiltViewModel()){
    val ownProfile = remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.loadUserData(id, context)
        ownProfile.value = ownProfile(id, context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).testTag("profile_screen")) {
                Column{
                    Details(navController, ownProfile)
                }
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
private fun Details(navController: NavController, ownProfile: MutableState<Boolean>){
    Box(Modifier.fillMaxSize()){
        val scroll = rememberScrollState(0)
        Header()
        Body(scroll, navController, ownProfile)
    }
}

@Composable
private fun Header(vm: UserProfileViewModel = hiltViewModel()){
    Box(modifier = Modifier
        .fillMaxSize()) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .blur(5.dp)
        CustomImage(imageModifier, vm.user.backgroundPictureFilePath, ImageSourceSelector.BACKGROUND)
    }
}

@Composable
private fun Body(scroll: ScrollState, navController: NavController, ownProfile: MutableState<Boolean>, vm: UserProfileViewModel = hiltViewModel()){
    val headerSize=100.dp
    val imageSize= 150.dp
    val padding=headerSize-imageSize/2
    Box(Modifier.fillMaxSize()){
        Column(Modifier.fillMaxSize()) {
            Spacer(
                Modifier
                    .height(headerSize)
                    .fillMaxWidth())
            Column(Modifier
                    .verticalScroll(scroll)
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(size = 30.dp)
                    )
            ) {

                Row(Modifier.fillMaxWidth().padding(end=10.dp, top=10.dp, bottom=25.dp),
                    horizontalArrangement = Arrangement.End){
                    val menuExpanded = remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    if(ownProfile.value){
                        Box{
                            IconButton(onClick = { menuExpanded.value= true }, modifier = Modifier.testTag("profile_dropdownMenu")) {
                                Icon(
                                    imageVector= Icons.Rounded.MoreVert,
                                    contentDescription = null
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded.value,
                                onDismissRequest = { menuExpanded.value = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                            ){
                                DropdownMenuItem(
                                    text={ Text("Liked travels", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.ListScreen.route+"?id=${vm.user._id}?type=likedTravels")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_favorite_24),
                                            contentDescription = null,
                                            tint= MaterialTheme.colorScheme.onSecondaryContainer
                                        )},
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                                DropdownMenuItem(
                                    text = { Text("Edit", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.UserUpdateScreen.route)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )},
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer).testTag("profile_dropdownMenu_edit")
                                )
                                DropdownMenuItem(
                                    text={ Text("Upload background image", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        navController.navigate(Screen.UploadImageScreen.route+"?id=${vm.user._id}&uploadImageType=${UploadImageType.BACKGROUND}")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_image_24),
                                            contentDescription = null,
                                            tint= MaterialTheme.colorScheme.onSecondaryContainer
                                        )},
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                                DropdownMenuItem(
                                    text = { Text("Log out", color= MaterialTheme.colorScheme.onSecondaryContainer) },
                                    onClick = {
                                        menuExpanded.value = false
                                        vm.logout(context, navController)
                                    },
                                    leadingIcon = {
                                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24), contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                                    },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                            }
                        }
                        }
                    else{
                        Box {
                            val isFollower = isFollower(vm.user.followerIds, context)
                            LikeButton(!isFollower,{ vm.followAction(context) } )
                        }
                    }
                }

                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    val name = if(vm.user.name==""){"-"} else{vm.user.name}
                    Text(name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.testTag("profile_name"),
                    )
                    Text(vm.user.username,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.testTag("profile_username")
                    )
                    Column(Modifier.fillMaxWidth().padding(horizontal = 25.dp, vertical = 10.dp)){
                        val email = if(vm.user.email==""){"-"} else{vm.user.email}
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = Icons.Rounded.MailOutline, contentDescription = null,tint= MaterialTheme.colorScheme.primary)
                            Text(email,
                                modifier = Modifier.padding(start=5.dp).testTag("profile_email"))
                        }
                        val livingLabel = (vm.user.city?: "-")+", "+(vm.user.country?: "-")
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_public_24), contentDescription = null,tint= MaterialTheme.colorScheme.primary)
                            Text(livingLabel,
                                modifier = Modifier.padding(start=5.dp).testTag("profile_livingLabel"))
                        }

                    }
                    Column(Modifier.fillMaxWidth().padding(horizontal = 25.dp, vertical = 10.dp)){
                        Text("About me", fontWeight = FontWeight.Thin)
                        Text(vm.user.description?:"...", modifier = Modifier.testTag("profile_description"))
                    }
                    ListItemDivider()
                    Row(modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly){
                        val items=arrayOf(
                            Item(vm.user.followerIds?.size ?:0, "follower"),
                            Item(vm.user.followingIds?.size ?: 0, "following"),
                            Item(vm.user.travelIds?.size ?: 0, "travel")
                        )

                        items.forEach {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {navController.navigate(Screen.ListScreen.route+"?id=${vm.user._id}?type=${it.label}")  }){
                                Text(it.value.toString(),
                                    fontSize=18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.testTag("profile_${it.label}")
                                )
                                Text(it.label,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                    ListItemDivider()
                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)){
                        if(ownProfile.value){
                            items(count=1){
                                TravelItem(navController, null)
                            }
                        }
                            items(vm.travels){
                                TravelItem(navController, it)
                            }
                    }
                    ListItemDivider()
                }
            }
        }
        Column {//Profile picture
            Spacer(
                Modifier
                    .height(padding)
                    .fillMaxWidth()
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape)
                        .clickable {
                            navController.navigate(Screen.UploadImageScreen.route+"?id=${vm.user._id}&uploadImageType=${UploadImageType.PROFILE}")
                        }
                ){
                    val imageModifier= Modifier.fillMaxSize()
                    CustomImage(imageModifier, vm.user.profilePictureFilePath, ImageSourceSelector.PROFILE)
                }
            }
        }
    }
}

@Composable
private fun TravelItem(navController: NavController, travel: Travel?, vm: UserProfileViewModel = hiltViewModel()){
    val route = if(travel == null){Screen.NewTravelScreen.route} else{Screen.TravelProfileScreen.route+"?id=${travel._id}"}
    Box(
        Modifier
            .height(100.dp)
            .width(120.dp)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { navController.navigate(route) }
            .testTag("profile_travelitem_${travel?._id?:"add"}")) {
        if(travel != null){
            val imageModifier = Modifier.blur(3.dp).fillMaxSize()
            CustomImage(imageModifier, travel.pictureFileName, ImageSourceSelector.TRAVEL, 0.6F)

            Column(
                Modifier
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    if(travel.public && vm.ownProfile){
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_remove_red_eye_24),
                            contentDescription = null,
                            tint= MaterialTheme.colorScheme.primary)
                    }
                }
                Column{

                Text( travel.name,
                    fontWeight = FontWeight.Bold,
                    fontSize=18.sp,
                    color=  MaterialTheme.colorScheme.primary
                )
                Text(travel.city+", "+travel.country,
                    fontSize=10.sp, color= MaterialTheme.colorScheme.primary)
                }
            }
        }else{
            Box(Modifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxSize()){
                Image(imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                    contentDescription = null,
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                    modifier = Modifier.fillMaxSize(),
                    alignment = Alignment.Center)
            }
        }
    }
}

private data class Item(val value: Int, val label: String)