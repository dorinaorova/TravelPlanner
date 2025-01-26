package com.androidlab.travelplannerapp.feature.uploadImage

import android.content.Intent.parseUri
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
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
                .padding(paddingValues)) {
                Column{
                    Body(navController)
                }
            }
        },
        topBar = {
            TopBar(navController, uploadImageType)
        },
        bottomBar ={
            BottomBar(navController)
        }
    )
}

@Composable
private fun Body(navController: NavController, vm: UploadImageViewModel = hiltViewModel()){
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        vm.imageUri = uri
    }

    Row {
        Button(onClick = {
            pickImageLauncher.launch("image/*")
        },
            modifier=Modifier.padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            colors= ButtonDefaults.buttonColors(containerColor = colorResource(R.color.secondary))
        ) {
            Icon(
                modifier = Modifier.padding(vertical = 10.dp),
                imageVector = ImageVector.vectorResource(R.drawable.baseline_image_24),
                contentDescription = "Upload photo",
                tint= colorResource(R.color.primary_text)
            )
        }
    }

    vm.imageUri?.let{
        AsyncImage(
            model = vm.imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, uploadImageType: UploadImageType ,vm: UploadImageViewModel = hiltViewModel()){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                val route = if(uploadImageType == UploadImageType.TRAVEL) {Screen.TravelProfileScreen.route}else{Screen.ProfileScreen.route}
                navController.navigate(route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back",
                    tint = colorResource(id = R.color.primary_text)
                )
            }
        },
        title = {
            val label= when(uploadImageType){
                UploadImageType.TRAVEL -> "Upload travel picture"
                UploadImageType.PROFILE -> "Upload profile picture"
                UploadImageType.BACKGROUND -> "Upload background picture"
            }
            Text(label)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = colorResource(id = R.color.primary_text),
        ),
    )
}

@Composable
private fun BottomBar(navController: NavController, vm: UploadImageViewModel = hiltViewModel()){
    val context = LocalContext.current
    BottomAppBar(
        containerColor = colorResource(id = R.color.primary),
        contentColor = colorResource(id = R.color.primary_text),
        content = {
            Button(
                onClick = {
                    vm.uploadImage(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = colorResource(id = R.color.primary_text)
                )
            ) {
                Text("Upload")
            }
        }

    )
}