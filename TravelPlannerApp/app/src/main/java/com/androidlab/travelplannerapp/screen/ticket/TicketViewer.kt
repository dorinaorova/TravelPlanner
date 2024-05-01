package com.androidlab.travelplannerapp.screen.ticket

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.screen.utils.TopBar

@Composable
fun TicketViewerScreen(navController: NavController){

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Details()
                }
            }
        },
        topBar = {
            TopBar("Museum-Emma")
        },
    )
}

@Composable
private fun Details(){
}


@Composable
@Preview(showBackground =  true)
fun TicketViewerScreenPreview(){
    TicketViewerScreen(navController = rememberNavController())
}