package com.androidlab.travelplannerapp.feature.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.BlankTravelImage
import com.androidlab.travelplannerapp.feature.utils.isFollower
import com.androidlab.travelplannerapp.feature.utils.ownProfile
import com.androidlab.travelplannerapp.feature.utils.profilePictureFilePath
import com.androidlab.travelplannerapp.feature.utils.travelPicturePath
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun SearchScreen(navController: NavController, vm: SearchViewModel = hiltViewModel()){
    val travelPicked = remember { mutableStateOf(false) }
    Scaffold(
        content={paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.primary_background))) {
                Column{
                    SearchBar(travelPicked)
                    if(travelPicked.value){
                        FilterBtn()
                        TravelSearchResultList(navController)
                    }else{
                        UserSearchResultList(navController)
                    }
                }
            }
        },
        bottomBar = {
            NavBar(navController)
        }
    )
}

@Composable
fun SearchBar(travelPicked: MutableState<Boolean>, vm: SearchViewModel = hiltViewModel()){
    Box(Modifier.background(colorResource(id = R.color.secondary))){
        Column{
            Row (Modifier.padding(top=10.dp, end=15.dp, start=15.dp)){
                var value by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary)
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            vm.searchUser(value)
                            keyboardController?.hide()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(id = R.color.primary_background),
                                    shape = RoundedCornerShape(size = 30.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = colorResource(id = R.color.secondary)
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            innerTextField()
                        }
                    }
                )
            }
            TravelOrUserPicker(travelPicked)
        }
    }
}

@Composable
fun TravelOrUserPicker(travelPicked: MutableState<Boolean>){
    val travelBtnColor: Color
    val userBtnColor : Color
    if(travelPicked.value){
        travelBtnColor= colorResource(id = R.color.primary_background)
        userBtnColor= colorResource(id = R.color.primary_text)
    }
    else{
        travelBtnColor= colorResource(id = R.color.primary_text)
        userBtnColor= colorResource(id = R.color.primary_background)
    }

    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){
        TextButton(onClick = { travelPicked.value=true }) {
            Text(text = "Travel",
                color = travelBtnColor)
        }
        TextButton(onClick = { travelPicked.value=false }) {
            Text(text = "User",
                color = userBtnColor)
        }
    }
}
@Composable
private fun FilterBtn(){
    OutlinedButton(onClick = {
    },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_background)),
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.secondary))
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_list_24),
            contentDescription = "filter",
            tint = colorResource(id = R.color.secondary)
        )
        Text(
            text="Filter",
            color = colorResource(id = R.color.secondary)
        )
    }
}

@Composable
private fun TravelListItem(navController: NavController, travel: Travel, vm: SearchViewModel = hiltViewModel()) {
    Row(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        Row(Modifier.clickable { navController.navigate(Screen.TravelProfileScreen.route+"?id=${travel._id}") }) {
            Box(
                Modifier
                    .width(90.dp)
                    .height(80.dp)
                    .padding(horizontal = 10.dp)
            ) {
                val imageModifier = Modifier.fillMaxSize()
                if(travel.pictureFileName != null){
                    AsyncImage(
                        model = travelPicturePath(context = LocalContext.current, travel.pictureFileName!!),
                        contentDescription = null,
                        modifier = imageModifier)
                }else{
                    BlankTravelImage(imageModifier)
                }
            }
            Column {
                Text(travel.name,
                        fontSize = 18.sp)
                Text("${travel.city}, ${travel.country}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start=8.dp))
                Text("${travel.price} ${travel.currency}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start=8.dp))
                Text("tag1, tag2",
                        fontSize = 8.sp,
                        modifier = Modifier.padding(start=10.dp, top= 10.dp))
            }
        }
        Box {
            val liked  = if (true) {
                ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
            }else {
                ImageVector.vectorResource(R.drawable.baseline_favorite_24)
            }
            IconButton(onClick = { /*TODO*/ }, Modifier.padding(end=20.dp)) {
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

@Composable
private fun UserListItem(navController: NavController, user: UserInfo, vm: SearchViewModel = hiltViewModel()){
    Row(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
        .clickable {
            navController.navigate(Screen.ProfileScreen.route+"?id=${user._id}")
                   },
        horizontalArrangement = Arrangement.SpaceBetween){
        Row {
            Box(
                Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            ) {
                if(user.profilePictureFilePath != ""){
                    AsyncImage(
                        model = profilePictureFilePath(context = LocalContext.current, user.profilePictureFilePath!!),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }else{
                    Image(
                        painterResource(id = R.drawable.blank_profile),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(Modifier.align(Alignment.CenterVertically).padding(start=10.dp)) {
                Text(user.username,
                    fontSize = 18.sp)
                Text(user.name,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start=8.dp))
            }
        }
        Box(Modifier.align(Alignment.CenterVertically).padding(end=20.dp)) {
            val context = LocalContext.current
            if(!ownProfile(user._id, context)){
                Box {
                    val isFollower = isFollower(user.followerIds, context)
                    val liked  = if (!isFollower) {
                        ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
                    }else {
                        ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                    }
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
}

@Composable
private fun TravelSearchResultList(navController: NavController, vm: SearchViewModel = hiltViewModel()){
    LaunchedEffect(Unit, block ={
        vm.getAllTravel()
    })
    LazyColumn {
        items(vm.travel) { item ->
            TravelListItem(navController, item)
            ListItemDivider()
        }
    }
}

@Composable
private fun ListItemDivider(){
    Divider(
        thickness = 1.dp,
        color = colorResource(id = R.color.primary),
        modifier = Modifier.padding(horizontal = 25.dp)
    )
}

@Composable
private fun UserSearchResultList(navController: NavController, vm: SearchViewModel = hiltViewModel()){
    LaunchedEffect(Unit, block ={
        vm.getAllUsers()
    })
    LazyColumn {
        items(vm.users) { item ->
            UserListItem(navController, item)
            ListItemDivider()
        }
    }
}


@Composable
@Preview(showBackground =  true)
fun SearchScreenPreview(){
    SearchScreen(navController = rememberNavController())
}