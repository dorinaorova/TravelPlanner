package com.androidlab.travelplannerapp.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.screen.navbar.NavBar

@Composable
fun SearchScreen(navController: NavController){

    Scaffold(
        content={paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.primary_background))) {
                Column{
                    SearchBar()
                    filterBtn()
                }
            }
        },
        bottomBar = {
            NavBar(navController)
        }
    )
}

@Composable
fun SearchBar(){
    Box(Modifier.background(colorResource(id = R.color.secondary))){
        Column{
            Row (Modifier.padding(top=10.dp)){
                var value by remember { mutableStateOf("") }
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(id = R.color.secondary)
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {}
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 10.dp)
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(id = R.color.primary_background),
                                    shape = RoundedCornerShape(size = 30.dp)
                                )
                                .padding(all = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon",
                                tint = colorResource(id = R.color.secondary)
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            innerTextField()
                        }
                    }
                )
            }
            TravelOrUserPicker()
        }
    }
}

@Composable
fun TravelOrUserPicker(){
    var travelPicked = true
    val travelBtnColor: Color
    val userBtnColor : Color
    if(travelPicked){
        travelBtnColor= colorResource(id = R.color.primary_background)
        userBtnColor= colorResource(id = R.color.primary_text)
    }
    else{
        travelBtnColor= colorResource(id = R.color.primary_text)
        userBtnColor= colorResource(id = R.color.primary_background)
    }

    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        TextButton(onClick = { travelPicked=true }) {
            Text(text = "Travel",
                color = travelBtnColor)
        }
        TextButton(onClick = { travelPicked=false }) {
            Text(text = "User",
                color = userBtnColor)
        }
    }
}
@Composable
private fun filterBtn(){
    OutlinedButton(onClick = {
    },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary_background)),
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.secondary))
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_list_24),
            contentDescription = "filter",
            tint = colorResource(id = R.color.secondary)
        )
        Text(
            text="Filter",
            color = colorResource(id = R.color.secondary)
        )
    }
}
@Composable
@Preview(showBackground =  true)
fun SearchScreenPreview(){
    SearchScreen(navController = rememberNavController())
}