@file:Suppress("NAME_SHADOWING")

package com.androidlab.travelplannerapp.feature.travelCreate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.feature.utils.InputField
import com.androidlab.travelplannerapp.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TravelCreateUpdateScreen(navController: NavController, id: String?,  vm: TravelCreateUpdateViewModel = hiltViewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit){
        if(id != null){
            //vm.fetchData(context)
        }
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Form(navController= navController)
                }
            }
        },
        topBar = {
            TopBar(navController, id)
        }
    )
}

@Composable
private fun Form(navController: NavController, vm: TravelCreateUpdateViewModel = hiltViewModel()){
    val name = remember(vm.travel.name) { mutableStateOf(vm.travel.name) }
    val city = remember(vm.travel.city) { mutableStateOf(vm.travel.city) }
    val country = remember(vm.travel.country) { mutableStateOf(vm.travel.country) }
    val description = remember(vm.travel.description?: "") { mutableStateOf(vm.travel.description?: "") }
    val tags = remember(vm.travel.tags) { mutableStateOf(vm.travel.tags) }
    val price = remember(vm.travel.price) { mutableStateOf(vm.travel.price) }
    val currency = remember(vm.travel.currency) { mutableStateOf(vm.travel.currency) }
    val startDate = remember(vm.travel.startDate) { mutableStateOf(vm.travel.startDate) }
    val endDate = remember(vm.travel.endDate) { mutableStateOf(vm.travel.endDate) }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())){
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.height(20.dp))
            InputField(name, KeyboardOptions(imeAction = ImeAction.Next), null, "Name", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(city, KeyboardOptions(imeAction = ImeAction.Next), null, "City", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(country, KeyboardOptions(imeAction = ImeAction.Next), null, "Country", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            Row {
                DatePickerForm("Start date", startDate)
                DatePickerForm("End date", endDate)
            }
            Spacer(Modifier.height(20.dp))
            //TODO
            Spacer(Modifier.height(20.dp))
            PriceCurrencyForm(price,currency )
            Spacer(Modifier.height(20.dp))
            InputField(description, KeyboardOptions(imeAction = ImeAction.Next), null, "Description",labelColor = colorResource(R.color.primary), lines = 4)
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                val travel = Travel(
                    _id = null,
                    name = name.value,
                    city = city.value,
                    country = country.value,
                    description = description.value,
                    tags = tags.value,
                    price = price.value,
                    currency = currency.value,
                    startDate = startDate.value,
                    endDate = endDate.value,
                    pictureFileName = null,
                    public = false,
                    userId = null
                )
               vm.save(travel, context)
            },
                Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = colorResource(id = R.color.primary_text)
                )
            ) { Text("Save") }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PriceCurrencyForm(price:MutableState<Int>, currency: MutableState<String>){
    val isError = price.value < 0
    val expanded = remember { mutableStateOf(false) }
    val currencyItems = listOf("HUF", "EUR", "USD")
    Column(modifier = Modifier.fillMaxWidth()){
        Text(
            "Price",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = if(isError){Color.Red}else{colorResource(id = R.color.primary)},
            modifier = Modifier.padding(start = 50.dp)
        )
        Spacer(Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.Center){
            BasicTextField(
                value = price.value.toString(),
                onValueChange = { price.value = it.toIntOrNull() ?: 0 },
                maxLines = 1,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.primary)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
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
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        innerTextField()
                    }
                }
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Box{
                Text(currency.value,
                    color = colorResource(R.color.secondary_text),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(onClick = { expanded.value = true })
                )
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.background(colorResource(id = R.color.primary_text))) {
                    currencyItems.forEach { i ->
                        DropdownMenuItem(
                            text={
                                Text(
                                    text = i,
                                    color = colorResource(R.color.primary)
                                )
                            },
                            onClick = {
                                expanded.value = false
                                currency.value = i
                            },
                            modifier = Modifier.fillMaxWidth().background(colorResource(id = R.color.primary_text))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DatePickerForm(label: String, date: MutableState<Long>){
    val showDatePicker = remember { mutableStateOf(false)}
    val dateText = if(date.value == 0L){"Select date"}else{
        convertMillisToDate(date.value)
    }
    Column(Modifier.padding(10.dp)) {
        Text(text = label, modifier = Modifier.padding(bottom = 5.dp).align(Alignment.CenterHorizontally), color = colorResource(id = R.color.primary))
        Button(onClick = { showDatePicker.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.primary_text),
                contentColor = colorResource(id = R.color.primary),
            )) {
            Text(text = dateText)
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
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?: Date().time

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd.")
    return formatter.format(Date(millis))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, id: String?){
    val title = if(id == null){"New travel"} else "Update travel"
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(Screen.ProfileScreen.route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back",
                    tint = colorResource(id = R.color.primary_text)
                )
            }
        },
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = colorResource(id = R.color.primary_text),
        ),
    )
}