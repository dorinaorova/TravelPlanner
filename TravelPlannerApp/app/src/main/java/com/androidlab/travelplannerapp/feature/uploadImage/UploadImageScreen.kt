package com.androidlab.travelplannerapp.feature.uploadImage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun UploadImageScreen(navController: NavController, id: String?, uploadImageTypeString: String, vm: UploadImageViewModel = hiltViewModel()){
    val uploadImageType = enumValueOf<UploadImageType>(uploadImageTypeString)
    LaunchedEffect(Unit){
        vm.id=id
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)) {
                Column{
                    Body(uploadImageType)
                }
            }
        },
        topBar = {
            val label= when(uploadImageType){
                UploadImageType.TRAVEL -> "Upload travel picture"
                UploadImageType.PROFILE -> "Upload profile picture"
                UploadImageType.BACKGROUND -> "Upload background picture"
            }
            val route = if(uploadImageType == UploadImageType.TRAVEL) {Screen.TravelProfileScreen.route}else{Screen.ProfileScreen.route}

            TopBar(label, navController, route)
        },
        bottomBar ={
            BottomBar(uploadImageType, navController)
        }
    )
}

@Composable
private fun Body(uploadImageType:UploadImageType ,vm: UploadImageViewModel = hiltViewModel()){
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        vm.imageUri = uri
    }

    Column(Modifier.fillMaxWidth()){
        Button(onClick = {
            pickImageLauncher.launch("image/*")
        },
            modifier=Modifier.padding(20.dp).align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 10.dp),
                imageVector = ImageVector.vectorResource(R.drawable.baseline_image_24),
                contentDescription = "Upload photo",
            )
        }
    }

    vm.imageUri?.let{
        Row( modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            val modifierForProfilePicture = Modifier.width(300.dp).height(300.dp).clip(CircleShape).border(5.dp, Color.White, CircleShape)
            Box(modifier = if(uploadImageType == UploadImageType.PROFILE) modifierForProfilePicture else Modifier.fillMaxWidth(0.9f)){
                AsyncImage(
                    model = vm.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
            )
            }
        }
    }

}

@Composable
private fun BottomBar(uploadImageType: UploadImageType,  navController: NavController, vm: UploadImageViewModel = hiltViewModel()){
    val context = LocalContext.current
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Column(Modifier.fillMaxWidth()) {

            Button(
                onClick = {
                    vm.uploadImage(context, uploadImageType, navController)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Upload")
            }
            }
        }

    )
}