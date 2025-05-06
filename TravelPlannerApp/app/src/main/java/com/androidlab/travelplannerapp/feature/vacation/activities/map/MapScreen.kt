package com.androidlab.travelplannerapp.feature.vacation.activities.map

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.ActivityType
import com.androidlab.travelplannerapp.feature.utils.Map
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.navigation.Screen
import com.google.android.gms.maps.model.LatLng


@Composable
fun MapScreen(navController: NavController, travelId: String, vm: MapViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.travelId = travelId
        vm.fetchData(travelId, context)
    }
    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = {
            showDialog.value = it
        })
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()){
                    val longClickAction = { latLng: LatLng ->
                        if(vm.ownTravel){
                            vm.coordsSelected(latLng)
                            showDialog.value = true
                        }
                    }
                Map(vm.markers, longClickAction, Modifier.fillMaxSize(), vm.loading, vm.travel.city, context)
            }
        },
        topBar = {
            val route = if(vm.ownTravel) Screen.VacationScreen.route + "?id=${travelId}" else Screen.TravelProfileScreen.route + "?id=${travelId}"
            TopBar(
                "Activities",
                navController,
                route,
                R.drawable.baseline_menu_24,
                Screen.ActivityListScreen.route + "?id=${travelId}"
            )
        },
    )


}

@Composable
private fun AddDialog(setShowDialog: (Boolean) -> Unit, vm: MapViewModel = hiltViewModel()){
    val expanded = remember { mutableStateOf(false) }
    val type = remember{ mutableStateOf<ActivityType?>(null) }
    val name = remember{ mutableStateOf("") }
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
                        color= MaterialTheme.colorScheme.primary
                    )
                    TextField(
                        value = name.value,
                        singleLine = true,
                        onValueChange = {
                            name.value = it
                        },
                        placeholder = { Text("Please enter the name",color= MaterialTheme.colorScheme.primary) })
                }
                Box(Modifier.fillMaxWidth().padding(10.dp)){
                    Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary).clickable(onClick = { expanded.value = true })){
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