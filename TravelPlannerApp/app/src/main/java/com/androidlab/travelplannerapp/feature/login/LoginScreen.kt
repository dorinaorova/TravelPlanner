package com.androidlab.travelplannerapp.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.InputField
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, vm : LoginViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit, block ={
        vm.checkRefreshToken(context, navController)
    })
            Box(modifier = Modifier
                .fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
            ) {
                    LoginForm(navController)
            }
}

@Composable
private fun LoginForm(navController: NavController, vm : LoginViewModel = hiltViewModel()){
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(Modifier
        .padding(bottom = 100.dp)
        .background(color=MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(size = 10.dp))){
        Column(Modifier.padding(horizontal = 10.dp)){
            Text("Login",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier =Modifier.padding(15.dp).align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(20.dp))

            InputField(username, KeyboardOptions(imeAction = ImeAction.Next), null, "Username", Icons.Rounded.AccountCircle, labelColor = MaterialTheme.colorScheme.onPrimaryContainer)

            Spacer(Modifier.height(20.dp))

            InputField(password,keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), PasswordVisualTransformation(), label="Password", icon = Icons.Rounded.Lock, labelColor = MaterialTheme.colorScheme.onPrimaryContainer)

            Spacer(Modifier.height(10.dp))
            Button(onClick={
                vm.login(username.value,password.value, context, navController)
            },
                Modifier.align(Alignment.CenterHorizontally)){
                Text("Login", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(30.dp))
            ClickableText(
                text = AnnotatedString("Sign up here"),
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                navController.navigate(route = Screen.RegistrationScreen.route) },
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
