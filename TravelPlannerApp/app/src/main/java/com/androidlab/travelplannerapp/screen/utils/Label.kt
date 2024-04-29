package com.androidlab.travelplannerapp.screen.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallHeader(text: String) {
    Text(text,
        fontSize=16.sp,
        fontWeight = FontWeight.Bold,
        modifier= Modifier.padding(start=25.dp, end=25.dp, top=15.dp, bottom = 5.dp))
}