package com.androidlab.travelplannerapp.screen.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
