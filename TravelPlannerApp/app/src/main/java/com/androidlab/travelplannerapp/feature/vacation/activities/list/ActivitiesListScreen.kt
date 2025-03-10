package com.androidlab.travelplannerapp.feature.vacation.activities.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.example.compose.primaryBackgroundCustom
import com.example.compose.primaryCustom
import com.example.compose.primaryTextCustom
import com.example.compose.secondaryCustom

@Composable
fun ActivityListScreen(navController: NavController, travelId: String,vm: ActivitiesListViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        vm.travelId = travelId
        vm.fetchData(travelId)
    }

    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = { showDialog.value = it})
    }


    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(primaryBackgroundCustom)) {
                ActivitiesList(navController)
            }
        },
        topBar = {
            TopBar(navController, travelId)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true}, containerColor = primaryCustom, contentColor = primaryTextCustom) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}

@Composable
private fun AddDialog(setShowDialog: (Boolean) -> Unit, vm: ActivitiesListViewModel = hiltViewModel()){
    val expanded = remember { mutableStateOf(false) }
    val type = remember{mutableStateOf<ActivityType?>(null)}
    val name = remember{mutableStateOf("")}
    val typeList = enumValues<ActivityType>().toList()
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = primaryBackgroundCustom,
            modifier = Modifier.padding(10.dp)
        ){
            Column (Modifier.fillMaxWidth(0.8f).padding(vertical=15.dp), verticalArrangement = Arrangement.SpaceBetween){
                Row(
                    Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Text("Name",
                        modifier=Modifier.padding(end=10.dp),
                        fontWeight= FontWeight.Bold,
                        color= colorResource(id = R.color.primary))
                    TextField(
                        value = name.value,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = { Text("Please enter the name")})
                }
                Box(Modifier.fillMaxWidth().padding(10.dp)){
                    Row(Modifier.fillMaxWidth().border(1.dp, secondaryCustom).clickable(onClick = { expanded.value = true })){
                            Text(text = type.value?.type?: "Select a type",
                                color = colorResource(R.color.secondary_text),
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                    }
                    DropdownMenu(expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(colorResource(id = R.color.primary_text))) {
                        typeList.forEach { i ->
                            DropdownMenuItem(
                                text={
                                    Text(
                                        text = i.type,
                                        color = colorResource(R.color.primary)
                                    )
                                },
                                onClick = {
                                    expanded.value = false
                                    type.value = i
                                },
                                modifier = Modifier.fillMaxWidth().background(colorResource(id = R.color.primary_text))
                            )
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        vm.addActivity(name.value, type.value!!)
                        setShowDialog(false)
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryCustom, contentColor = primaryTextCustom),
                        enabled = type.value != null){
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivitiesList(navController: NavController, vm: ActivitiesListViewModel = hiltViewModel()){
    LazyColumn() {
        items(vm.activities){
            ActivityListItem(it)
        }
    }
}

@Composable
private fun ActivityListItem(activity: Activity, vm: ActivitiesListViewModel = hiltViewModel()){
    Row (Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 3.dp), horizontalArrangement = Arrangement.SpaceBetween){
        Row{
        Icon(imageVector = ImageVector.vectorResource(id = iconForActivityType(activity.type)), contentDescription = "activity type")
        Text(activity.name)
        }
        IconButton(onClick = {vm.deleteActivity(activity.id!!)}) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.cancel), contentDescription = "delete activity")

        }
    }
}

fun iconForActivityType(type: ActivityType): Int{
    return when(type){
        ActivityType.RESTAURANT->{
            R.drawable.baseline_restaurant_24
        }
        ActivityType.SHOP->{
            R.drawable.baseline_shopping_basket_24
        }
        ActivityType.MUSEUM->{
            R.drawable.baseline_museum_24
        }
        ActivityType.CAFE -> {
            R.drawable.baseline_local_cafe_24
        }
        ActivityType.BAR -> {
            R.drawable.baseline_local_bar_24
        }
        ActivityType.STATUE -> {
            R.drawable.baseline_add_a_photo_24
        }
        else -> {
            R.drawable.baseline_location_pin_24
        }
    }
}

@Composable
private fun TopBar(navController: NavController, travelId: String){

}