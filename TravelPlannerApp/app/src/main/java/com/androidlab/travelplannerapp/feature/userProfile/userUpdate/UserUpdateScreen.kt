package com.androidlab.travelplannerapp.feature.userProfile.userUpdate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.InputField
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun UserUpdateScreen(navController: NavController, vm: UserUpdateViewModel = hiltViewModel()){
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Form()
                }
            }
        },
        topBar = {
            TopBar(navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(Screen.ProfileScreen.route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back",
                    tint = colorResource(id = R.color.primary_text)
                )
            }
        },
        title = {
            Text("Update profile")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = colorResource(id = R.color.primary_text),
        ),
    )
}


@Composable
private fun Form(){
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.height(20.dp))
            InputField(name, KeyboardOptions(imeAction = ImeAction.Next), null, "Name", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(description, KeyboardOptions(imeAction = ImeAction.Next), null, "Description",labelColor = colorResource(R.color.primary), lines = 4)
            Spacer(Modifier.height(20.dp))
            InputField(city, KeyboardOptions(imeAction = ImeAction.Next), null, "City",labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(country, KeyboardOptions(imeAction = ImeAction.Done), null, "Country",labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            Button(onClick = { /*TODO*/ },
                Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = colorResource(id = R.color.primary_text)
                )
            ) { Text("Save") }
        }
    }
}

@Composable
@Preview(showBackground =  true)
fun UserUpdateScreenPreview(){
    UserUpdateScreen(navController = rememberNavController())
}