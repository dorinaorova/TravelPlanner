package com.androidlab.travelplannerapp.feature.userProfile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.navigation.Screen
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.uploadImage.UploadImageType
import com.androidlab.travelplannerapp.feature.utils.CustomDivider
import com.androidlab.travelplannerapp.feature.utils.isFollower
import com.androidlab.travelplannerapp.feature.utils.ownProfile
import com.androidlab.travelplannerapp.feature.utils.profilePictureFilePath

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
                .padding(paddingValues)) {
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
        if(vm.user.backgroundPictureFilePath != ""){
            val context = LocalContext.current
            AsyncImage(
                model = vm.backgroundPicturePath(context),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .blur(5.dp),
            )
        }else{
            Image(
                painterResource(id = R.drawable.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .blur(5.dp),
            )
        }
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
                        colorResource(id = R.color.primary_background),
                        shape = RoundedCornerShape(size = 30.dp)
                    )
            ) {

                Row(Modifier.fillMaxWidth().padding(end=10.dp, top=10.dp, bottom=25.dp),
                    horizontalArrangement = Arrangement.End){
                    val menuExpanded = remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    if(ownProfile.value){
                    Box{
                        IconButton(onClick = { menuExpanded.value= true }) {
                            Icon(
                                imageVector= Icons.Rounded.MoreVert,
                                contentDescription = null,
                                tint= colorResource(id = R.color.primary)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false },
                            modifier = Modifier.background(colorResource(id = R.color.primary_text))
                        ){
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    menuExpanded.value = false
                                    navController.navigate(Screen.UserUpdateScreen.route)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_24),
                                        contentDescription = null
                                    )},
                                modifier = Modifier.background(colorResource(id = R.color.primary_text))
                            )
                            DropdownMenuItem(
                                text={ Text("Upload background image") },
                                onClick = {
                                    menuExpanded.value = false
                                    navController.navigate(Screen.UploadImageScreen.route+"?id=${vm.user._id}&uploadImageType=${UploadImageType.BACKGROUND}")
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_image_24),
                                        contentDescription = null
                                    )},
                                modifier = Modifier.background(colorResource(id = R.color.primary_text))
                            )
                            DropdownMenuItem(
                                text = { Text("Log out") },
                                onClick = {
                                    menuExpanded.value = false
                                    vm.logout(context, navController)
                                },
                                leadingIcon = {
                                    Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_logout_24), contentDescription = null)
                                },
                                modifier = Modifier.background(colorResource(id = R.color.primary_text))
                            )
                        }
                    }
                        }
                    else{
                        Box {
                            val isFollower = isFollower(vm.user.followerIds, context)
                            val liked  = if (!isFollower) {
                                ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
                            }else {
                                ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                            }
                            IconButton(onClick = { vm.followAction(context) }, Modifier.padding(end=20.dp)) {
                                Icon(
                                    imageVector = liked,
                                    contentDescription = "like",
                                    tint = colorResource(id = R.color.primary),
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                )
                            }
                        }
                    }
                }

                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    val name = if(vm.user.name==""){"-"} else{vm.user.name}
                    Text(name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(vm.user.username,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.secondary_text)
                    )
                    Column(Modifier.fillMaxWidth().padding(horizontal = 25.dp, vertical = 10.dp)){
                        val email = if(vm.user.email==""){"-"} else{vm.user.email}
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = Icons.Rounded.MailOutline, contentDescription = null,tint= colorResource(id = R.color.primary))
                            Text(email,
                                modifier = Modifier.padding(start=5.dp))
                        }
                        val livingLabel = (vm.user.city?: "-")+", "+(vm.user.country?: "-")
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_public_24), contentDescription = null,tint= colorResource(id = R.color.primary))
                            Text(livingLabel,
                                modifier = Modifier.padding(start=5.dp))
                        }

                    }
                    Column(Modifier.fillMaxWidth().padding(horizontal = 25.dp, vertical = 10.dp)){
                        Text("About me", fontWeight = FontWeight.Thin, color = colorResource(id = R.color.primary))
                        Text(vm.user.description?:"...")
                    }
                    CustomDivider()
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Text(it.value.toString(),
                                    fontSize=18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(it.label,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                    CustomDivider()
                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)){
                            items(count=6){
                                TravelItem(navController)
                            }
                    }
                    Divider(thickness = 1.dp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(horizontal=25.dp, vertical=20.dp))
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

                if(vm.user.profilePictureFilePath != ""){
                    val context = LocalContext.current
                    AsyncImage(
                        model=profilePictureFilePath(context, vm.user.profilePictureFilePath!!),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()

                    )
                }else {
                    Image(
                        painterResource(id = R.drawable.blank_profile),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()

                    )
                }
                }
            }
        }
    }
}

@Composable
private fun TravelItem(navController: NavController){
    Box(
        Modifier
            .height(100.dp)
            .width(120.dp)
            .padding(horizontal = 10.dp).clickable { navController.navigate(Screen.TravelProfileScreen.route) }) {
        Image(painter = painterResource(id = R.drawable.image), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier= Modifier
                .clip(RoundedCornerShape(10.dp))
                .blur(3.dp))
        Column(
            Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom) {
            Text("Krakow",
                color= colorResource(id = R.color.primary_text),
                fontWeight = FontWeight.Bold,
                fontSize=18.sp
            )
            Text("Krakow, Poland",
                color= colorResource(id = R.color.primary_text),
                fontSize=10.sp)
        }
    }
}


@Composable
@Preview(showBackground =  true)
fun ProfileScreenPreview(){
    ProfileScreen(navController = rememberNavController(), null)
}

private data class Item(val value: Int, val label: String)