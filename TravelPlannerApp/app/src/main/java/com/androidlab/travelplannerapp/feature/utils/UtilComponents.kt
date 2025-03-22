package com.androidlab.travelplannerapp.feature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Activity
import com.example.compose.primaryCustom
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