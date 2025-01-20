package com.androidlab.travelplannerapp.feature.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.InputField


@Composable
fun RegistrationScreen(navController: NavController, vm : RegistrationViewModel = hiltViewModel()){
    Box(modifier = Modifier
        .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        RegistrationForm(navController)
    }
}

@Composable
private fun RegistrationForm(navController: NavController, vm: RegistrationViewModel = hiltViewModel()){
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(Modifier
        .padding(bottom = 100.dp)
        .background(color= colorResource(R.color.secondary), shape = RoundedCornerShape(size = 10.dp))){
        Column(Modifier.padding(horizontal = 10.dp)){
            Text("Create new account",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary_background),
                modifier =Modifier.padding(15.dp))
            Spacer(Modifier.height(20.dp))
            InputField(username, KeyboardOptions(imeAction = ImeAction.Next), label ="Username")
            Spacer(Modifier.height(20.dp))
            InputField(name, KeyboardOptions(imeAction = ImeAction.Next), label="Full name")
            Spacer(Modifier.height(20.dp))
            InputField(email, KeyboardOptions(imeAction = ImeAction.Next), label= "Email", isError = !vm.checkEmailFormat(email.value))
            Spacer(Modifier.height(20.dp))
            InputField(password,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), PasswordVisualTransformation(), label="Password")
            Spacer(Modifier.height(20.dp))
            Button(onClick={
                vm.signUp(username.value,password.value, name.value, email.value, context, navController)
            },
                Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_background))){
                Text("Ready to travel!", color= colorResource(R.color.secondary), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}


@Composable
@Preview(showBackground =  true)
fun RegistrationScreenPreview(){
    RegistrationScreen(navController = rememberNavController())
}