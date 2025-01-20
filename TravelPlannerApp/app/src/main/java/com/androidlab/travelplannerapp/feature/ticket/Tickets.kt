package com.androidlab.travelplannerapp.feature.ticket

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.navigation.Screen
import com.androidlab.travelplannerapp.feature.utils.SmallHeader
import com.androidlab.travelplannerapp.feature.utils.TopBar

@Composable
fun TicketsScreen(navController: NavController){
    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = { showDialog.value = it})
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
            TopBar("Tickets", navController, Screen.VacationScreen.route)
        },
        floatingActionButton = {
            IconButton(onClick = { /*TODO*/ },
                modifier= Modifier.background(colorResource(id = R.color.secondary), shape= CircleShape)) {
                Icon(imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint= colorResource(id = R.color.primary_text),
                    modifier= Modifier.size(40.dp)
                )
            }
        }
    )
}
@Composable
private fun AddDialog(setShowDialog: (Boolean) -> Unit,){
    val nameTxtField = remember { mutableStateOf("") }
    val ticketTxtField = remember { mutableStateOf("") }
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
                        Text("Name",
                            modifier=Modifier.padding(end=10.dp),
                            fontWeight= FontWeight.Bold,
                            color= colorResource(id = R.color.primary))
                        TextField(
                            value = nameTxtField.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            onValueChange = {
                                nameTxtField.value = it
                            },
                            placeholder = { Text("Please enter the name") })
                    }
                    Button(onClick = { setShowDialog(false) },
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
private fun Details(){
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary_background))){
        LazyColumn(Modifier.padding(20.dp).fillMaxWidth()) {
            items(3){
                Column(Modifier.padding(bottom=15.dp)) {
                    SmallHeader("2024.05.01.")
                    LazyRow(Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
                        items((it + 1) * 2) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(80.dp, 60.dp)
                                    .background(
                                        colorResource(id = R.color.secondary),
                                        shape = RoundedCornerShape(15.dp)
                                    )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        "Museum",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        "Emma",
                                        fontSize = 10.sp
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

@Composable
@Preview(showBackground =  true)
fun TicketsScreenPreview(){
    TicketsScreen(navController = rememberNavController())
}