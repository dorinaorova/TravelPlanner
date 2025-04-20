package com.androidlab.travelplannerapp.feature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Activity
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.feature.search.SearchViewModel
import com.androidlab.travelplannerapp.navigation.Screen
import com.example.compose.primaryCustom
import java.util.Date
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun SmallHeader(text: String) {
    Text(text,
        fontSize=16.sp,
        fontWeight = FontWeight.Bold,
        color = primaryCustom,
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
fun TopBar(label: String, navController: NavController, route: String, secondaryIcon: Int? = null, secondaryRoute: String? = null){
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.secondary)),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
        Row{
            IconButton(onClick = { navController.navigate(route) }) {
                Icon(imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = null,
                    tint= colorResource(id = R.color.primary_text))
            }
            androidx.compose.material3.Text(label,
                fontSize=18.sp,
                modifier= Modifier.padding(vertical = 20.dp))
        }
        if(secondaryIcon != null && secondaryRoute != null){
            IconButton(onClick = { navController.navigate(secondaryRoute) }) {
                Icon(imageVector = ImageVector.vectorResource(secondaryIcon),
                    contentDescription = null,
                    tint= colorResource(id = R.color.primary_text))
            }
        }

    }
}

@Composable
fun InputField(_value: MutableState<String>, keyboardOptions: KeyboardOptions, visualTransformation: VisualTransformation? = null, label:String, icon: ImageVector? = null, isError: Boolean = false, labelColor: Color = colorResource(id = R.color.primary_background), lines: Int = 1){
    val focusManager = LocalFocusManager.current
    Column{
    androidx.compose.material3.Text(
        label,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = if(isError){Color.Red}else{labelColor},
        modifier = Modifier.padding(start = 15.dp)
    )
    Spacer(Modifier.height(5.dp))
    BasicTextField(
        value = _value.value,
        onValueChange = { _value.value = it },
        maxLines = lines,
        minLines = lines,
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.primary)
        ),
        visualTransformation = visualTransformation ?: VisualTransformation.None,
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
                        color = if (isError) {
                            Color.Red
                        } else {
                            colorResource(id = R.color.primary)
                        },
                        shape = RoundedCornerShape(size = 10.dp)

                    ).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
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
}


@Composable
fun BlankTravelImage(imageModifier: Modifier, source: ImageSourceSelector = ImageSourceSelector.TRAVEL ){
    val image = if(source == ImageSourceSelector.PROFILE){R.drawable.blank_profile}else{R.drawable.blank_travel_image}
    Image(
        painterResource(id = image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
}

@Composable
fun ListItemDivider(){
    Divider(
        thickness = 1.dp,
        color = colorResource(id = R.color.primary),
        modifier = Modifier.padding(horizontal = 25.dp)
    )
}

@Composable
fun CustomImage(imageModifier: Modifier, filePath: String?, imageSource: ImageSourceSelector = ImageSourceSelector.TRAVEL){
    if(!filePath.isNullOrEmpty()){
        val fullPicturePath = when(imageSource){
            ImageSourceSelector.PROFILE -> profilePictureFilePath(LocalContext.current ,filePath)
            ImageSourceSelector.BACKGROUND -> backgroundPicturePath(LocalContext.current, filePath)
            else -> travelPicturePath(LocalContext.current, filePath)
        }
        AsyncImage(
            model = fullPicturePath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    }else{
        BlankTravelImage(imageModifier, imageSource)
    }
}

@Composable
fun DatePickerForm(label: String, date: MutableState<Long>){
    val showDatePicker = remember { mutableStateOf(false) }
    val dateText = if(date.value == 0L){"Select date"}else{
        generateDate(date.value)
    }
    Column(Modifier.padding(10.dp)) {
        androidx.compose.material3.Text(
            text = label,
            modifier = Modifier.padding(bottom = 5.dp).align(Alignment.CenterHorizontally),
            color = colorResource(id = R.color.primary)
        )
        Button(onClick = { showDatePicker.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.primary_text),
                contentColor = colorResource(id = R.color.primary),
            )) {
            androidx.compose.material3.Text(text = dateText)
        }
    }

    if (showDatePicker.value) {
        MyDatePickerDialog(
            onDateSelected = { date.value = it },
            onDismiss = { showDatePicker.value = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
    })

    val selectedDate = datePickerState.selectedDateMillis?: Date().time

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            androidx.compose.material3.Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                androidx.compose.material3.Text(text = "OK")
            }
        },
        dismissButton = {
            androidx.compose.material3.Button(onClick = {
                onDismiss()
            }) {
                androidx.compose.material3.Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


enum class ImageSourceSelector {
    TRAVEL,
    PROFILE,
    BACKGROUND
}

@Composable
fun CustomMaker(activity: Activity){
    val singaporeMarkerState = rememberMarkerState(position = LatLng(activity.latitude!!, activity.longitude!!))
    val context = LocalContext.current
    Marker(
        state = singaporeMarkerState,
        title = activity.name,
        snippet = activity.type.toString(),
        icon = bitmapFromVector(context, iconForActivityType(activity.type))
    )
}

private fun bitmapFromVector(context: Context, @DrawableRes vectorResId: Int): BitmapDescriptor {
    val markerDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_location_pin_24)!!
    markerDrawable.setTint(android.graphics.Color.rgb(0, 80, 76))
    val customIconDrawable = ContextCompat.getDrawable(context, vectorResId)!!
    customIconDrawable.setTint(android.graphics.Color.WHITE)

    val width = markerDrawable.intrinsicWidth*2
    val height = markerDrawable.intrinsicHeight*2

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    markerDrawable.setBounds(0, 0, width, height)
    markerDrawable.draw(canvas)

    val holePaint = Paint().apply {
        color = android.graphics.Color.rgb(0, 80, 76)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val centerX = width / 2f
    val centerY = height / 3f
    val holeRadius = width / 5f

    canvas.drawCircle(centerX, centerY, holeRadius, holePaint)

    val iconSize = width / 2
    val left = (width - iconSize) / 2
    val top = (height - iconSize) / 3

    customIconDrawable!!.setBounds(left, top, left + iconSize, top + iconSize)
    customIconDrawable!!.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
fun Map(markers: List<Activity>, longClickAction: (LatLng) -> Unit, boxModifier: Modifier, isLoading: MutableState<Boolean>, city: String, context: Context){
    Box(modifier = boxModifier) {
        if(isLoading.value){
            Text("Loading...")
        }else {
            val center = getLatLngFromCity(context, city) ?: LatLng(47.49, 19.04)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(center, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    longClickAction(latLng)
                }
            ) {
                markers.forEach { marker ->
                    CustomMaker(marker)
                }
            }
        }
    }
}

@Composable
fun TravelListItem(navController: NavController, travel: Travel, ownTravel: Boolean, liked: Boolean, likeTravel: Unit) {
    Row(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        Row(Modifier.clickable { navController.navigate(Screen.TravelProfileScreen.route+"?id=${travel._id}") }) {
            Box(
                Modifier
                    .width(90.dp)
                    .height(80.dp)
                    .padding(horizontal = 10.dp)
            ) {
                val imageModifier = Modifier.fillMaxSize()
                CustomImage(imageModifier, travel.pictureFileName, ImageSourceSelector.TRAVEL)
            }
            Column {
                Text(travel.name,
                    fontSize = 18.sp)
                Text("${travel.city}, ${travel.country}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start=8.dp))
                Text("${travel.price} ${travel.currency}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start=8.dp))
                Text(travel.tags?.joinToString(separator = ", ")?: "",
                    fontSize = 8.sp,
                    modifier = Modifier.padding(start=10.dp, top= 10.dp))
            }
        }
        Box {
            if(!ownTravel){
                val likedIcon  = if (liked) {
                    ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                }else {
                    ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
                }
                androidx.compose.material.IconButton(onClick = {
//                    likeTravel(
//                        travel._id!!,
//                        context
//                    )
                }, Modifier.padding(end = 20.dp)) {
                    androidx.compose.material.Icon(
                        imageVector = likedIcon,
                        contentDescription = "like",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UserListItem(navController: NavController, user: UserInfo){
    Row(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.ProfileScreen.route+"?id=${user._id}")
            },
        horizontalArrangement = Arrangement.SpaceBetween){
        Row {
            Box(
                Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            ) {
                val imageModifier = Modifier.fillMaxSize()
                CustomImage(imageModifier, user.profilePictureFilePath, ImageSourceSelector.PROFILE)
            }
            Column(Modifier.align(Alignment.CenterVertically).padding(start=10.dp)) {
                Text(user.username,
                    fontSize = 18.sp)
                Text(user.name,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start=8.dp))
            }
        }
        Box(Modifier.align(Alignment.CenterVertically).padding(end=20.dp)) {
            val context = LocalContext.current
            if(!ownProfile(user._id, context)){
                Box {
                    val isFollower = isFollower(user.followerIds, context)
                    val liked  = if (!isFollower) {
                        ImageVector.vectorResource(R.drawable.baseline_favorite_border_24)
                    }else {
                        ImageVector.vectorResource(R.drawable.baseline_favorite_24)
                    }
                    androidx.compose.material.Icon(
                        imageVector = liked,
                        contentDescription = "like",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )

                }
            }
        }
    }
}