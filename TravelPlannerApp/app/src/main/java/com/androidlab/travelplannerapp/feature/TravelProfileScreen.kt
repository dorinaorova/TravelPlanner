package com.androidlab.travelplannerapp.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.navbar.NavBar

@Composable
fun TravelProfileScreen(navController: NavController){
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Details(navController)
            }
        },
        bottomBar ={
            NavBar(navController)
        }
    )
}

@Composable
private fun Details(navController: NavController){
    Box(Modifier.fillMaxSize()){
        val scroll = rememberScrollState(0)
        Header()
        Body(scroll, navController)
    }
}

@Composable
private fun Header(){
    Box(modifier = Modifier
        .fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .blur(5.dp),
        )
        Column(Modifier.padding(top = 75.dp)) {
            Text(
                "Krakow",
                color = colorResource(id = R.color.primary_text),
                fontSize = 36.sp,
                modifier = Modifier.padding(start = 25.dp),
                fontFamily = FontFamily(Font(R.font.itim))
            )
            Row(Modifier.padding(horizontal = 25.dp, vertical = 10.dp)) {
                val list = arrayOf("Poland", "Shoprt trip")
                list.forEach {
                    TagItem(it)
                    Spacer(Modifier.width(10.dp))
                }
            }
        }
    }
}

@Composable
private fun TagItem(text: String){
    Box(Modifier.background(
        colorResource(id = R.color.primary),
        shape= RoundedCornerShape(10.dp))
        .padding(horizontal=5.dp, vertical=3.dp)
    ){
        Text(text,
            color= colorResource(id = R.color.primary_text))
    }
}
@Composable
private fun Body(scroll: ScrollState, navController: NavController){
    Column{
        Spacer(
            Modifier
                .height(160.dp)
                .fillMaxWidth())
        Column(
            Modifier
                .verticalScroll(scroll)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    colorResource(id = R.color.primary_background),
                    shape = RoundedCornerShape(size = 30.dp)
                )){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp, horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Column(Modifier.padding(start=12.dp)) {
                    DataRow("Krakow, Poland", ImageVector.vectorResource(R.drawable.baseline_map_24))
                    DataRow("3 days (May 3 - May 6)", ImageVector.vectorResource(R.drawable.baseline_calendar_month_24))
                    DataRow("200-300$", ImageVector.vectorResource(R.drawable.baseline_attach_money_24))
                    DataRow("by plane", ImageVector.vectorResource(R.drawable.baseline_directions_bus_24))
                }
                val liked  = if (true) {
                    ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
                }else {
                    ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = liked,
                        contentDescription = "like",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp))
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp, end = 25.dp, start = 25.dp)){
                Text("Description",
                        fontSize = 8.sp,
                        color = colorResource(id = R.color.secondary_text)
                )
                Text("Cat ipsum dolor sit amet, trip owner up in kitchen i want food. Scratch at door to be let outside, get let out then scratch at door immmediately after to be let back in hide from vacuum cleaner eat plants, meow, and throw up because i ate plants. Thinking about you i'm joking it's food always food paw at beetle and eat it before it gets away so who's the baby, so ha ha, you're funny i'll kill you last.",
                    fontSize = 10.sp,
                    modifier= Modifier.padding(horizontal=5.dp))
            }
            Map()
            PackingList()
        }
    }
}

@Composable
private fun DataRow(text: String, icon: ImageVector){
    Row(Modifier.padding(bottom=8.dp),
        verticalAlignment = Alignment.CenterVertically){
        Icon(imageVector = icon,
            contentDescription = text,
            Modifier.padding(end=6.dp))
        Text(text)
    }
}

@Composable
private fun Map(){
    Box(
        Modifier
            .padding(start = 25.dp, end = 25.dp, bottom = 25.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(
                colorResource(id = R.color.primary),
                shape = RoundedCornerShape(size = 30.dp)
            ))
}

@Composable
private fun PackingList(){
    Column(Modifier.padding(start=25.dp, end=25.dp, bottom=25.dp)){
        Text("What should you bring",
            fontSize=16.sp,
            fontWeight = FontWeight.Bold,
            modifier=Modifier.padding(bottom=8.dp))
        var list = arrayOf("item1", "item2", "item3", "item4", "item5", "item6", "item7")
        val bullet = "\u2022"
        val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
        Text(
            buildAnnotatedString {
                list.forEach {
                    withStyle(style = paragraphStyle) {
                        append(bullet)
                        append("\t\t")
                        append(it)
                    }
                }
            },
            modifier=Modifier.padding(start=10.dp)
        )
    }
}


@Composable
@Preview(showBackground =  true)
fun TravelProfileScreenPreview(){
    TravelProfileScreen(navController = rememberNavController())
}