package com.androidlab.travelplannerapp.feature.userProfile.userUpdate

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.InputField
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun UserUpdateScreen(navController: NavController, vm: UserUpdateViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        vm.fetchData(context)
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).testTag("user_update_screen")) {
                Column{
                    Form(navController= navController)
                }
            }
        },
        topBar = {
            TopBar("Update profile", navController, Screen.ProfileScreen.route)
        }
    )
}

@Composable
private fun Form(vm: UserUpdateViewModel = hiltViewModel(), navController: NavController){
    val name = remember(vm.user.name) { mutableStateOf(vm.user.name) }
    val description = remember(vm.user.description?: "") { mutableStateOf(vm.user.description?: "") }
    val email = remember(vm.user.email) { mutableStateOf(vm.user.email) }
    val city = remember(vm.user.city?: "") { mutableStateOf(vm.user.city?: "") }
    val country = remember(vm.user.country?: "") { mutableStateOf(vm.user.country?: "") }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.height(20.dp))
            InputField(name, KeyboardOptions(imeAction = ImeAction.Next), null, "Name", labelColor = MaterialTheme.colorScheme.primary, testTag = "user_update_name")
            Spacer(Modifier.height(20.dp))
            InputField(email, KeyboardOptions(imeAction = ImeAction.Next), null, "Email", labelColor = MaterialTheme.colorScheme.primary, testTag = "user_update_email")
            Spacer(Modifier.height(20.dp))
            InputField(description, KeyboardOptions(imeAction = ImeAction.Next), null, "Description",labelColor = MaterialTheme.colorScheme.primary, lines = 4,testTag = "user_update_description")
            Spacer(Modifier.height(20.dp))
            InputField(city, KeyboardOptions(imeAction = ImeAction.Next), null, "City",labelColor = MaterialTheme.colorScheme.primary, testTag = "user_update_city")
            Spacer(Modifier.height(20.dp))
            InputField(country, KeyboardOptions(imeAction = ImeAction.Done), null, "Country",labelColor = MaterialTheme.colorScheme.primary, testTag = "user_update_country")
            Spacer(Modifier.height(20.dp))
            Button(onClick = { vm.updateUser(name.value, email.value, description.value, city.value, country.value, navController) },
                Modifier.align(Alignment.CenterHorizontally).testTag("user_update_save"),
            ) { Text("Save") }
        }
    }
}
