package com.androidlab.travelplannerapp.screen

import android.media.Image
import android.text.InputType
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R

@Composable
fun LoginScreen(navController: NavController){
            Box(modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                    LoginForm()
            }
}

@Composable
fun LoginForm(){
    var username =""
    var password=""
    Box(Modifier
        .padding(bottom = 100.dp)
        .background(color=colorResource(R.color.secondary), shape = RoundedCornerShape(size = 10.dp))){
        Column(Modifier.padding(horizontal = 10.dp)){
            Text("Login",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary_background),
                modifier =Modifier.padding(15.dp).align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(20.dp))
            InputField(username,KeyboardOptions(imeAction = ImeAction.Next), "Username", Icons.Rounded.AccountCircle )
            Spacer(Modifier.height(20.dp))
            InputField(password, KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Next), "Password", Icons.Rounded.Lock)
            Spacer(Modifier.height(10.dp))
            Button(onClick={},
                Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_background))){
                Text("Login", color= colorResource(R.color.secondary), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(30.dp))
            ClickableText(
                text = AnnotatedString("Sign up here"),
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {/* navController.navigate(route = Screen.SignUpScreen.route) */},
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = colorResource(id = R.color.primary_background)
                )
            )
        }
    }
}

@Composable
private fun InputField(_value: String, keyboardOptions: KeyboardOptions, label:String, icon: ImageVector){
    val focusManager = LocalFocusManager.current
    var value=_value
    Text(
        label,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(id = R.color.primary_background),
        modifier = Modifier.padding(start = 15.dp)
    )
    Spacer(Modifier.height(5.dp))
    BasicTextField(
        value = value,
        onValueChange = { value = it },
        maxLines = 1,
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.primary)
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        color = colorResource(R.color.primary_background),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.primary),
                        shape = RoundedCornerShape(size = 10.dp)

                    ) .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorResource(id = R.color.primary)
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                innerTextField()
            }
        }
    )
}

@Composable
@Preview(showBackground =  true)
fun LoginScreenPreview(){
    LoginScreen(navController = rememberNavController())
}