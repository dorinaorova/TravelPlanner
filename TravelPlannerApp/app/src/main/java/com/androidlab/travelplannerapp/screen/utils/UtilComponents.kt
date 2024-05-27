package com.androidlab.travelplannerapp.screen.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
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
