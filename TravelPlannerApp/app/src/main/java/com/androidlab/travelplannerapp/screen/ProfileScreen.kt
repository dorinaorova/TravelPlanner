package com.androidlab.travelplannerapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.screen.navbar.NavBar
import com.androidlab.travelplannerapp.screen.utils.CustomDivider

@Composable
fun ProfileScreen(navController: NavController){
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Details(navController)
                }
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
    }
}

@Composable
private fun Body(scroll: ScrollState, navController: NavController){
    val headerSize=100.dp
    val imageSize= 150.dp
    val padding=headerSize-imageSize/2
    Box{
        Column {
            Spacer(
                Modifier
                    .height(headerSize)
                    .fillMaxWidth())
            Column(
                Modifier
                    .verticalScroll(scroll)
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.primary_background),
                        shape = RoundedCornerShape(size = 30.dp)
                    )
            ) {
                Row(Modifier.fillMaxWidth().padding(end=10.dp, top=10.dp, bottom=25.dp),
                    horizontalArrangement = Arrangement.End){
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector= ImageVector.vectorResource(id = R.drawable.baseline_edit_24),
                            contentDescription = null,
                            tint= colorResource(id = R.color.primary)
                        )
                    }
                }
                Column(Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Emma Philips",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text("Los Angeles, CA",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.secondary_text)
                    )
                    Text("Cat ipsum dolor sit amet, why must they do that or eat a rug and furry furry hairs everywhere oh no human coming lie on counter don't get off counter sit on the laptop",
                        modifier = Modifier.padding(start=25.dp, end=25.dp, top = 15.dp))
                    CustomDivider()
                    Row(modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly){
                        val items=arrayOf(
                            Item(200, "follower"),
                            Item(115, "following"),
                            Item(15, "travel")
                        )

                        items.forEach {
                            Column(horizontalAlignment = Alignment.CenterHorizontally){
                                Text(it.value.toString(),
                                    fontSize=18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(it.label,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                    CustomDivider()
                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)){
                            items(count=6){
                                TravelItem()
                            }
                    }
                    Divider(thickness = 1.dp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(horizontal=25.dp, vertical=20.dp))
                }
            }
        }
        Column {//Profile picture
            Spacer(
                Modifier
                    .height(padding)
                    .fillMaxWidth()
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .height(imageSize)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
private fun TravelItem(){
    Box(
        Modifier
            .height(100.dp)
            .width(120.dp)
            .padding(horizontal = 10.dp)){
        Image(painter = painterResource(id = R.drawable.image), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier= Modifier
                .clip(RoundedCornerShape(10.dp))
                .blur(3.dp))
        Column(
            Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom) {
            Text("Krakow",
                color= colorResource(id = R.color.primary_text),
                fontWeight = FontWeight.Bold,
                fontSize=18.sp
            )
            Text("Krakow, Poland",
                color= colorResource(id = R.color.primary_text),
                fontSize=10.sp)
        }
    }
}


@Composable
@Preview(showBackground =  true)
fun ProfileScreenPreview(){
    ProfileScreen(navController = rememberNavController())
}

private data class Item(val value: Int, val label: String)