package com.androidlab.travelplannerapp.feature.ticket.ticketListView

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.DatePickerForm
import com.androidlab.travelplannerapp.feature.utils.SmallHeader
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen
import com.example.compose.primaryCustom
import com.example.compose.primaryTextCustom

@Composable
fun TicketsScreen(navController: NavController, id: String, vm: TicketViewModel = hiltViewModel()){
    LaunchedEffect(Unit) {
        vm.setTravelId(id)
        vm.fetchData()
    }
    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        CreateTicketDialog(setShowDialog = { showDialog.value = it})
    }

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Details()
                }
            }
        },
        topBar = {
            TopBar("Tickets", navController, Screen.VacationScreen.route+"?id=$id")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true}, containerColor = primaryCustom, contentColor = primaryTextCustom) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}

@Composable
private fun CreateTicketDialog(setShowDialog: (Boolean) -> Unit, vm: TicketViewModel = hiltViewModel()){
    val date = remember{ mutableLongStateOf(0L) }
    val ticketTxtField = remember { mutableStateOf("") }
    val context = LocalContext.current
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ){
            Box(Modifier.width(300.dp)){
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add new ticket",
                            fontSize=16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }
                    Row(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Text("Ticket",
                            modifier=Modifier.padding(end=10.dp),
                            fontWeight= FontWeight.Bold,
                            color= colorResource(id = R.color.primary))
                        TextField(
                            value = ticketTxtField.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            onValueChange = {
                                ticketTxtField.value = it
                            },
                            placeholder = { Text("Please enter the occasion") })
                    }
                    Row(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        DatePickerForm("Date", date)
                    }
                    Button(onClick = {
                        vm.createTicket(ticketTxtField.value,date.longValue, context)
                        setShowDialog(false) },
                        modifier=Modifier.padding(bottom=10.dp),
                        colors= ButtonDefaults.buttonColors(colorResource(id = R.color.primary))) {
                        Text("Add",
                            color= Color.White)
                    }
                }
            }
        }

    }
}

@Composable
private fun Details(vm: TicketViewModel = hiltViewModel()){
    val context = LocalContext.current
    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary_background))){
        if(vm.tickets.isNotEmpty()) {
            LazyColumn(Modifier.padding(20.dp).fillMaxWidth()) {
                items(vm.tickets) { ticket ->
                    Column(Modifier.padding(bottom = 15.dp)) {
                        Row{
                            SmallHeader("${generateDate(ticket.date)} - ${ticket.name}")
                            IconButton(onClick = { launcher.launch(arrayOf("application/pdf")) }) {
                                androidx.compose.material.Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                                    contentDescription = null,
                                    tint = colorResource(id = R.color.primary),
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                )
                            }
                            result.value?.let{
                                vm.uploadTicket(it, LocalContext.current, ticket._id!!)
                                result.value = null
                            }
                        }
                        LazyRow(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                        items(ticket.files) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(80.dp, 60.dp)
                                    .background(
                                        colorResource(id = R.color.secondary),
                                        shape = RoundedCornerShape(15.dp)
                                    ).clickable {
                                        vm.downloadTicket(it, context)
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val icon = if(vm.fileExists(it)){ ImageVector.vectorResource(R.drawable.baseline_open_in_new_24) } else{ ImageVector.vectorResource(R.drawable.baseline_save_alt_24)}
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = "",
                                        tint = colorResource(id = R.color.primary_text)
                                    )
                                }
                            }
                        }
                        }
                    }
                }
            }
        }
    }
}