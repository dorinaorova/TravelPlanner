package com.androidlab.travelplannerapp.feature.search

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar
import com.androidlab.travelplannerapp.feature.utils.ListItemDivider
import com.androidlab.travelplannerapp.feature.utils.TravelListItem
import com.androidlab.travelplannerapp.feature.utils.UserListItem


val openFilterDialog =mutableStateOf(false)

@Composable
fun SearchScreen(navController: NavController, vm: SearchViewModel = hiltViewModel()){
    val context = LocalContext.current
    val travelPicked = remember { mutableStateOf(true) }
    when{
        openFilterDialog.value -> {
            FilterDialog(
                onDismissRequest = { openFilterDialog.value = false },
                vm
            )
        }
    }
    LaunchedEffect(Unit, block ={
        vm.getFilterValues()
        vm.getOwnUserData(context)
    })
    Scaffold(
        content={paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)) {
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
    Box(Modifier.background(MaterialTheme.colorScheme.primaryContainer)){
        Column{
            Row (Modifier.padding(top=10.dp, end=15.dp, start=15.dp)){
                val keyboardController = LocalSoftwareKeyboardController.current
                BasicTextField(
                    value = vm.searchName.value,
                    onValueChange = { vm.searchName.value = it },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if(travelPicked.value){
                                vm.searchTravel()
                            }else{
                                vm.searchUser()
                            }
                            keyboardController?.hide()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(size = 30.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = MaterialTheme.colorScheme.primary
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
        travelBtnColor= MaterialTheme.colorScheme.onPrimaryContainer
        userBtnColor= MaterialTheme.colorScheme.background
    }
    else{
        travelBtnColor= MaterialTheme.colorScheme.background
        userBtnColor= MaterialTheme.colorScheme.onPrimaryContainer
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
        openFilterDialog.value = true
    },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(8.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_list_24),
            contentDescription = "filter"
        )
        Text(
            text="Filter"
        )
    }
}


@Composable
private fun TravelSearchResultList(navController: NavController, vm: SearchViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit, block ={
        vm.getAllTravel()
    })
    LazyColumn {
        items(vm.travel) { item ->
            TravelListItem(navController, item,vm.isTravelOwn(item._id!!), vm.isTravelLiked(item._id)
            ) { vm.likeTravel(item._id, context) }
            ListItemDivider()
        }
    }
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
private fun FilterDialog(onDismissRequest: () -> Unit, vm: SearchViewModel = hiltViewModel()){
    var tag by remember {mutableStateOf("")}
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier
            .wrapContentSize()
            .padding(10.dp).background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(16.dp)) {
            Box{
            Column(Modifier.padding(10.dp)){
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text("Cancel", modifier = Modifier.clickable { onDismissRequest() }, color = MaterialTheme.colorScheme.secondary)
                    Text("Clear", modifier = Modifier.clickable {
                        vm.country.value = ""
                        vm.city.value = ""
                        tag = ""
                        vm.priceSliderPosition.value = 0f..100f
                        vm.daysSliderPosition.value = 0f..100f
                        vm.tagList.clear()
                        vm.priceSliderPosition.value = 0f..100f
                        vm.daysSliderPosition.value= 0f..100f
                        onDismissRequest()
                        vm.filterTravel()
                    }, color = MaterialTheme.colorScheme.secondary)
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Column{
                        Text("Country", modifier = Modifier.padding(start=5.dp), color = MaterialTheme.colorScheme.primary)
                        BasicTextField(
                            value = vm.country.value,
                            onValueChange = { vm.country.value = it },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            maxLines = 1,
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color =MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(size = 10.dp)
                                        )
                                        .padding(10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = RoundedCornerShape(size = 10.dp)

                                        )
                                        .padding(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                    Column{
                        Text("City",  modifier = Modifier.padding(start=5.dp), color = MaterialTheme.colorScheme.primary)
                        BasicTextField(
                            value = vm.city.value,
                            onValueChange = { vm.city.value = it },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            maxLines = 1,
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(size = 10.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = RoundedCornerShape(size = 10.dp)

                                        )
                                        .padding(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                }

                Text("Price", color = MaterialTheme.colorScheme.primary)
                RangeSlider(
                    value = vm.priceSliderPosition.value,
                    onValueChange = { vm.priceSliderPosition.value = it },
                    valueRange = 0f..100f,
                    steps = vm.calculateSteps(false),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
                Text(text=vm.priceFilterValue())
                Spacer(modifier = Modifier.height(15.dp))
                Text("Days", color = MaterialTheme.colorScheme.primary)
                RangeSlider(
                    value = vm.daysSliderPosition.value,
                    onValueChange = { vm.daysSliderPosition.value = it },
                    valueRange = 0f..100f,
                    steps = vm.calculateSteps(true),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
                Text(text=vm.daysFilterValue())
                Spacer(modifier = Modifier.height(15.dp))
                Text("Tags", color= MaterialTheme.colorScheme.primary)
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                    BasicTextField(
                        value = tag,
                        onValueChange = { tag = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(size = 10.dp)
                                    )
                                    .padding(10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(size = 10.dp)

                                    )
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                innerTextField()
                            }
                        }
                    )

                        Button(
                            onClick = {vm.tagList.add(tag)
                                      tag = ""},
                            enabled = tag != ""
                            ) {
                            Text("Add")
                        }
                    }
                LazyRow{
                    items(vm.tagList) {item->
                        InputChip(
                            onClick = {
                                vm.tagList.remove(item)
                            },
                            label = { Text(item, modifier = Modifier.padding(2.dp)) },
                            selected = false,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Localized description",
                                    Modifier.size(InputChipDefaults.AvatarSize)
                                )
                            },
                            modifier = Modifier.padding(end = 10.dp))

                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {
                        vm.filterTravel()
                        onDismissRequest()
                    },
                        ) {
                        Text("Apply")
                    }
                }
                }
            }

        }
    }
}


@Composable
@Preview(showBackground =  true)
fun SearchScreenPreview(){
    SearchScreen(navController = rememberNavController())
}