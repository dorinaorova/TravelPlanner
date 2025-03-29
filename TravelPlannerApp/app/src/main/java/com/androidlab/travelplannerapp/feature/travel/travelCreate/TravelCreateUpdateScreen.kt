package com.androidlab.travelplannerapp.feature.travel.travelCreate

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.feature.utils.DatePickerForm
import com.androidlab.travelplannerapp.feature.utils.InputField
import com.androidlab.travelplannerapp.feature.utils.generateDate
import com.androidlab.travelplannerapp.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TravelCreateUpdateScreen(navController: NavController, id: String?,  vm: TravelCreateUpdateViewModel = hiltViewModel()){
    LaunchedEffect(Unit){
        if(id != null){
            vm.fetchData(id)
        }
    }
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Column{
                    Form(navController= navController, isUpdateAction = id != null)
                }
            }
        },
        topBar = {
            TopBar(navController, id)
        }
    )
}

@Composable
private fun Form(navController: NavController, isUpdateAction: Boolean, vm: TravelCreateUpdateViewModel = hiltViewModel()){
    val name = remember(vm.travel.name) { mutableStateOf(vm.travel.name) }
    val city = remember(vm.travel.city) { mutableStateOf(vm.travel.city) }
    val country = remember(vm.travel.country) { mutableStateOf(vm.travel.country) }
    val description = remember(vm.travel.description?: "") { mutableStateOf(vm.travel.description?: "") }
    val tags = remember(vm.travel.tags) { mutableStateOf(vm.travel.tags) }
    val price = remember(vm.travel.price) { mutableStateOf(vm.travel.price) }
    val currency = remember(vm.travel.currency) { mutableStateOf(vm.travel.currency) }
    val startDate = remember(vm.travel.startDate) { mutableStateOf(vm.travel.startDate) }
    val endDate = remember(vm.travel.endDate) { mutableStateOf(vm.travel.endDate) }
    val isPublic = remember(vm.travel.public) { mutableStateOf(vm.travel.public) }

    val context = LocalContext.current
    val travel = Travel(
        _id = vm.travel._id,
        name = name.value,
        city = city.value,
        country = country.value,
        description = description.value,
        tags = tags.value,
        price = price.value,
        currency = currency.value,
        startDate = startDate.value,
        endDate = endDate.value,
        pictureFileName = vm.travel.pictureFileName,
        public = isPublic.value,
        ownerId = vm.travel.ownerId
    )

    Column(modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())){
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.height(20.dp))
            InputField(name, KeyboardOptions(imeAction = ImeAction.Next), null, "Name*", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(city, KeyboardOptions(imeAction = ImeAction.Next), null, "City*", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            InputField(country, KeyboardOptions(imeAction = ImeAction.Next), null, "Country*", labelColor = colorResource(R.color.primary))
            Spacer(Modifier.height(20.dp))
            PrivateCheckBox(isPublic)
            Spacer(Modifier.height(20.dp))
            Row {
                DatePickerForm("Start date*", startDate)
                DatePickerForm("End date*", endDate)
            }
            Spacer(Modifier.height(20.dp))
            TagList()
            Spacer(Modifier.height(20.dp))
            PriceCurrencyForm(price,currency )
            Spacer(Modifier.height(20.dp))
            InputField(description, KeyboardOptions(imeAction = ImeAction.Next), null, "Description",labelColor = colorResource(R.color.primary), lines = 4)
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                if(isUpdateAction){
                    vm.update(travel, navController)
                }else{
                    vm.save(travel, context, navController)
                }

            },
                Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.primary),
                    contentColor = colorResource(id = R.color.primary_text)
                ),
                enabled = vm.verifyTravelForm(travel)
            ) { Text("Save") }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PrivateCheckBox(isPublic: MutableState<Boolean>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "Public: ",
            color = colorResource(R.color.primary)
        )
        Checkbox(
            checked = isPublic.value,
            onCheckedChange = { isPublic.value = it },
            colors = androidx.compose.material3.CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.primary),
                uncheckedColor = colorResource(R.color.primary)
            )
        )
    }
}

@Composable
private fun TagList(vm: TravelCreateUpdateViewModel = hiltViewModel()){
    var tag by remember {mutableStateOf("")}
    Column (Modifier.fillMaxWidth().padding(horizontal = 40.dp), horizontalAlignment = Alignment.Start){
        Text(
            "Tags",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color =colorResource(id = R.color.primary),
            modifier = Modifier.padding(start = 10.dp)
        )
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
            BasicTextField(
                value = tag,
                onValueChange = { tag = it },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.primary)
                ),
                maxLines = 1,
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
                                color = colorResource(id = R.color.primary),
                                shape = RoundedCornerShape(size = 10.dp)

                            ).padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        innerTextField()
                    }
                }
            )

            Button(
                onClick = {vm.tagList.add(tag)
                    tag = ""},
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary)),
                enabled = tag != ""
            ) {
                androidx.compose.material.Text("Add", color = colorResource(id = R.color.primary_text))
            }
        }
        LazyRow{
            items(vm.tagList) {item->
                InputChip(
                    onClick = {
                        vm.tagList.remove(item)
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = colorResource(id = R.color.primary)
                    ),
                    label = { androidx.compose.material.Text(item, modifier = Modifier.padding(2.dp), color = colorResource(id = R.color.primary_text)) },
                    selected = false,
                    trailingIcon = {
                        androidx.compose.material.Icon(
                            Icons.Default.Close,
                            contentDescription = "Localized description",
                            Modifier.size(InputChipDefaults.AvatarSize),
                            tint = colorResource(id = R.color.primary_text)
                        )
                    },
                    modifier = Modifier.padding(end = 10.dp))

            }
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
        Row(Modifier.fillMaxWidth().padding(start = 40.dp), horizontalArrangement = Arrangement.Start){
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController, id: String?){
    val title = if(id == null){"New travel"} else "Update travel"
    val route = if(id == null){Screen.ProfileScreen.route} else {Screen.TravelProfileScreen.route+"?id=$id"}
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
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
            Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = colorResource(id = R.color.primary_text),
        ),
    )
}