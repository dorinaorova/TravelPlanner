package com.androidlab.travelplannerapp.feature.vacation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.SpendType
import com.androidlab.travelplannerapp.navigation.Screen
import com.androidlab.travelplannerapp.feature.utils.CustomDivider
import com.androidlab.travelplannerapp.feature.utils.SmallHeader
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.feature.utils.generateDate


@Composable
fun PaymentsScreen(navController: NavController, travelId: String, vm: PaymentViewModel = hiltViewModel()){
    LaunchedEffect(Unit) {
        vm.fetchData(travelId)
    }

    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = { showDialog.value = it})
    }

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
            TopBar("Payments", navController, Screen.VacationScreen.route+"?id=${travelId}")
        },
        floatingActionButton = {
            IconButton(onClick = { showDialog.value = true},
                modifier=Modifier.background(colorResource(id = R.color.secondary), shape= CircleShape)) {
                Icon(imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint= colorResource(id = R.color.primary_text),
                    modifier=Modifier.size(40.dp)
                    )
            }
        }
    )
}

@Composable
private fun Details(){
    Column(
        Modifier
            .background(colorResource(id = R.color.primary_background))
            .fillMaxSize()){
        Payments()
        CustomDivider()
        Sum()
        CustomDivider()
        SettleDebt()
    }
}

@Composable
private fun Payments(vm: PaymentViewModel = hiltViewModel()){
    val context = LocalContext.current
    SmallHeader("Payments")
    LazyColumn(
        Modifier
            .padding(horizontal = 30.dp)
            .height(200.dp)){
        items(vm.payments){
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Row(Modifier.padding(bottom = 5.dp)) {
                        Text(
                            "${vm.findUser(it.userId, context)} paid: ",
                            fontSize = 16.sp
                        )
                        Text(
                            text = it.cost.toString(),
                            color = colorResource(id = R.color.primary),
                            fontSize = 16.sp,
                        )
                        Text(
                            text= generateDate(it.date),
                            fontSize = 12.sp,
                            modifier=Modifier.padding(start=10.dp)
                        )
                    }
                    Row(Modifier.padding(bottom = 5.dp, start = 8.dp)) {
                        Text(
                            "To: ",
                            color = colorResource(id = R.color.primary)
                        )
                        val to = it.partUserIds.joinToString(", ") { user -> vm.findUser(user, context) }
                        Text(to)
                    }
                    Row(Modifier.padding(bottom = 10.dp, start = 8.dp)) {
                        Text(
                            "For: ",
                            color = colorResource(id = R.color.primary)
                        )
                        Text(it.type.toString())
                    }
                }
                IconButton(onClick = { vm.deletePayment(it._id!!) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.cancel),
                        contentDescription = null,
                        tint= colorResource(id = R.color.secondary))
                }
            }
        }
    }
}

@Composable
private fun Sum(vm: PaymentViewModel = hiltViewModel()){
    val debts = vm.calculateDebt()
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.Bottom) {
        SmallHeader("Sum")
        Text(vm.calculateSum().toString(),
            color= colorResource(id = R.color.primary),
            modifier=Modifier.padding(bottom=10.dp),
            fontWeight = FontWeight.Bold
        )
    }
    LazyVerticalGrid(columns=GridCells.Fixed(2),
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(150.dp)){
        items(debts.size){
            Row(Modifier.padding(bottom=10.dp)){
                Text(vm.findUser(debts.keys.elementAt(it), context),
                    modifier=Modifier.padding(end=5.dp))
                Text(debts.values.elementAt(it).toString(),
                    color= colorResource(id = R.color.primary))
            }
        }
    }
}

@Composable
private fun SettleDebt(vm: PaymentViewModel = hiltViewModel()){
    val context = LocalContext.current
    SmallHeader(text = "Settle Debt")
    LazyVerticalGrid(columns=GridCells.Fixed(2),
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(150.dp)){
        items(vm.transactions){
            Row(Modifier.padding(bottom=10.dp),
                verticalAlignment = Alignment.Top){
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        vm.findUser(it.fromUser, context),
                        fontSize = 14.sp
                    )
                    Text(
                        it.amount.toString(),
                        color = colorResource(id = R.color.primary),
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_forward),
                    contentDescription = null,
                    tint = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                )
                Text(
                    vm.findUser(it.toUser, context),
                    fontSize = 14.sp
                )
                IconButton(onClick = { vm.settleDebt(it) }) {
                    Icon(imageVector = Icons.Rounded.Check ,
                        contentDescription = null,
                        tint= colorResource(id = R.color.secondary))
                }
            }
        }
    }
}

@Composable
private fun AddDialog(setShowDialog: (Boolean) -> Unit, vm: PaymentViewModel = hiltViewModel()){
    val costTxtField = remember { mutableStateOf("") }
    val reasonsList = SpendType.entries.map { it.name }.dropLast(1)
    val selectedReason = remember { mutableStateOf(reasonsList[0]) }
    val selectedIndex = remember { mutableStateOf(0 )}
    val checkedStates = remember(vm.participants.size) { mutableStateListOf(*Array(vm.participants.size) { true }) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ){
            Box(Modifier.width(300.dp)){
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add new payment",
                            fontSize=16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.cancel),
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }
                    Row(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()){
                        val expanded = remember { mutableStateOf(false) }
                        Text("Who paid",
                            modifier=Modifier.padding(end=10.dp),
                            color= colorResource(id = R.color.primary),
                            fontWeight= FontWeight.Bold)
                        val username = if (vm.participants.isNotEmpty()) {
                            vm.participants[selectedIndex.value].username
                        } else {
                            "?"
                        }
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.CenterVertically)) {
                            Text(username,
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = { expanded.value = true }),
                                color= colorResource(id = R.color.secondary_text))
                            DropdownMenu(expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }) {
                                vm.participants.forEachIndexed { index, item ->
                                    DropdownMenuItem(onClick = {
                                        selectedIndex.value = index
                                        expanded.value = false
                                    }) {
                                        Text(item.username)
                                    }

                                }

                            }
                        }
                    }
                    Row(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Text("Cost",
                            modifier=Modifier.padding(end=10.dp),
                            fontWeight= FontWeight.Bold,
                            color= colorResource(id = R.color.primary))
                        TextField(
                            value = costTxtField.value,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            onValueChange = {
                                costTxtField.value = it
                            },
                            placeholder = { Text("Please enter the value")})
                    }
                    Row(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()){
                        val expanded = remember { mutableStateOf(false) }
                        Text("For what",
                            modifier=Modifier.padding(end=10.dp),
                            color= colorResource(id = R.color.primary),
                            fontWeight= FontWeight.Bold)
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.CenterVertically)) {
                            Text(selectedReason.value,
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = { expanded.value = true }),
                                color= colorResource(id = R.color.secondary_text))
                            DropdownMenu(expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }) {
                                reasonsList.forEach { item ->
                                    DropdownMenuItem(onClick = {
                                        selectedReason.value = item
                                        expanded.value = false
                                    }) {
                                        Text(item)
                                    }

                                }

                            }
                        }
                    }
                    Column(
                        Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth()){
                        Text("For who",
                            modifier=Modifier.padding(end=10.dp),
                            fontWeight= FontWeight.Bold,
                            color= colorResource(id = R.color.primary))
                        LazyVerticalGrid(columns=GridCells.Fixed(2),
                                        modifier=Modifier.height(150.dp)){
                            items(vm.participants.size){idx->
                                Row(verticalAlignment = Alignment.CenterVertically){
                                    Checkbox(
                                        checked = checkedStates[idx],
                                        onCheckedChange = { checkedStates[idx] = it }
                                    )
                                    Text(vm.participants[idx].username)
                                }
                            }
                        }
                    }
                    Button(
                        enabled = costTxtField.value!="" && checkedStates.any { it },
                        onClick = {
                        vm.addPayment(vm.participants[selectedIndex.value]._id!!,
                            vm.participants
                            .mapIndexedNotNull { index, participant ->
                                if (checkedStates[index]) participant._id else null
                            }, costTxtField.value.toDouble(), selectedReason.value)
                        setShowDialog(false) },
                            modifier=Modifier.padding(bottom=10.dp),
                            colors= ButtonDefaults.buttonColors(colorResource(id = R.color.primary))) {
                        Text("Add",
                            color= Color.White)
                    }
                }
            }
        }

    }
}