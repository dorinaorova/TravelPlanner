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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.feature.utils.iconForActivityType
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun ActivityListScreen(navController: NavController, travelId: String,vm: ActivitiesListViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.travelId = travelId
        vm.fetchData(travelId, context)
    }

    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = { showDialog.value = it})
    }


    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(MaterialTheme.colorScheme.background).testTag("activityListScreen")) {
                ActivitiesList(navController)
            }
        },
        topBar = {
            val route = if(vm.participant.value) Screen.VacationScreen.route + "?id=${travelId}" else Screen.TravelProfileScreen.route + "?id=${travelId}"
            TopBar(
                "Activities",
                navController,
                route,
                R.drawable.baseline_map_24,
                Screen.MapScreen.route + "?id=${travelId}"
            )
        },
        floatingActionButton = {
            if(vm.participant.value){
                FloatingActionButton(onClick = { showDialog.value = true}) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
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
            color = MaterialTheme.colorScheme.background,
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
                        color= MaterialTheme.colorScheme.primary)
                    TextField(
                        value = name.value,
                        singleLine = true,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = { Text("Please enter the name", color= MaterialTheme.colorScheme.background)})
                }
                Box(Modifier.fillMaxWidth().padding(10.dp)){
                    Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)).clickable(onClick = { expanded.value = true })){
                            Text(text = type.value?.type?: "Select a type",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                    }
                    DropdownMenu(expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
                        typeList.forEach { i ->
                            DropdownMenuItem(
                                text={
                                    Text(
                                        text = i.type,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                },
                                onClick = {
                                    expanded.value = false
                                    type.value = i
                                },
                                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        vm.addActivity(name.value, type.value!!)
                        setShowDialog(false)
                    },
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
    val opacity = if(activity.visited) 0.5f else 1f
    Row (Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 5.dp).alpha(opacity), horizontalArrangement = Arrangement.SpaceBetween){
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically){
            if(vm.participant.value){
                Checkbox(
                    checked = activity.visited,
                    onCheckedChange = { vm.visitActivity(activity.id!!) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
            Icon(imageVector = ImageVector.vectorResource(id = iconForActivityType(activity.type)), contentDescription = "activity type", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(horizontal = 5.dp))
            Text(activity.name, color = MaterialTheme.colorScheme.primary)
        }
        if(vm.participant.value){
            IconButton(onClick = {vm.deleteActivity(activity.id!!)}) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.cancel), contentDescription = "delete activity", tint = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}


