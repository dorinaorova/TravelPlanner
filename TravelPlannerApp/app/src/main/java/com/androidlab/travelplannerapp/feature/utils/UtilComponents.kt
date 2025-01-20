package com.androidlab.travelplannerapp.feature.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R

@Composable
fun SmallHeader(text: String) {
    Text(text,
        fontSize=16.sp,
        fontWeight = FontWeight.Bold,
        modifier= Modifier.padding(start=25.dp, end=25.dp, top=15.dp, bottom = 10.dp))
}

@Composable
fun CustomDivider(){
    Divider(
        thickness = 1.dp,
        color = colorResource(id = R.color.primary),
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 20.dp)
    )
}

@Composable
fun TopBar(label: String, navController: NavController, route: String){
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.secondary)),
        verticalAlignment = Alignment.CenterVertically){
        IconButton(onClick = { navController.navigate(route) }) {
            Icon(imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                contentDescription = null,
                tint= colorResource(id = R.color.primary_text))
        }
        androidx.compose.material3.Text(label,
            fontSize=18.sp,
            modifier= Modifier.padding(vertical = 20.dp))
    }
}

@Composable
fun InputField(_value: MutableState<String>, keyboardOptions: KeyboardOptions, visualTransformation: VisualTransformation? = null, label:String, icon: ImageVector? = null, isError: Boolean = false){
    val focusManager = LocalFocusManager.current
    val value=_value
    androidx.compose.material3.Text(
        label,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = if(isError){Color.Red}else{colorResource(id = R.color.primary_background)},
        modifier = Modifier.padding(start = 15.dp)
    )
    Spacer(Modifier.height(5.dp))
    BasicTextField(
        value = value.value,
        onValueChange = { value.value = it },
        maxLines = 1,
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.primary)
        ),
        visualTransformation = visualTransformation?: VisualTransformation.None,
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
                        color = if(isError){
                            Color.Red}
                        else{
                            colorResource(id = R.color.primary)
                        },
                        shape = RoundedCornerShape(size = 10.dp)

                    ) .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(icon!=null){
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colorResource(id = R.color.primary)
                    )
                }
                Spacer(modifier = Modifier.width(width = 8.dp))
                innerTextField()
            }
        }
    )
}
