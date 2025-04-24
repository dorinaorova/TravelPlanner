package com.androidlab.travelplannerapp.feature.vacation.invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.Status
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.feature.utils.CustomImage
import com.androidlab.travelplannerapp.feature.utils.ImageSourceSelector
import com.androidlab.travelplannerapp.feature.utils.ListItemDivider
import com.androidlab.travelplannerapp.feature.utils.TopBar
import com.androidlab.travelplannerapp.navigation.Screen

@Composable
fun InvitationScreen(navController: NavController, travelId: String, vm : InvitationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.travelId = travelId
        vm.fetchData(travelId, context)
    }

    val showDialog =  remember { mutableStateOf(false) }
    if(showDialog.value){
        AddDialog(setShowDialog = { showDialog.value = it})
    }


    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).background(MaterialTheme.colorScheme.background)) {
                InvitationList(navController)
            }
        },
        topBar = {
            TopBar(
                "Invitations",
                navController,
                Screen.VacationScreen.route + "?id=${travelId}"
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}
@Composable
private fun AddDialog(setShowDialog: (Boolean) -> Unit, vm: InvitationViewModel = hiltViewModel()){
    val expanded = remember { mutableStateOf(false) }
    val selectedUser = remember(null){mutableStateOf<UserInfo?>(null)}
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(10.dp)
        ){
            Column (Modifier.fillMaxWidth(0.8f).padding(vertical=15.dp), verticalArrangement = Arrangement.SpaceBetween){
                Box(Modifier.fillMaxWidth().padding(10.dp)){
                    Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary).clickable(onClick = { expanded.value = true })){
                        if(selectedUser.value != null){
                            UserData(selectedUser.value!!)
                        }else{
                            Text("Select a user",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }
                    }
                    DropdownMenu(expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
                        vm.users.forEach { i ->
                            DropdownMenuItem(
                                text={
                                    Text(
                                        text = i.username,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer)

                                },
                                onClick = {
                                    expanded.value = false
                                    selectedUser.value = i
                                },
                                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer)
                            )
                        }
                    }
                }
                val context = LocalContext.current
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    Button(onClick = {
                        vm.inviteUser(selectedUser.value!!, context)
                        setShowDialog(false)
                    },
                        enabled = selectedUser.value != null){
                        Text("Invite")
                    }
                }
            }
        }
    }
}

@Composable
private fun InvitationList(navController: NavController, vm: InvitationViewModel = hiltViewModel()){
    Column(Modifier.fillMaxHeight().padding(horizontal = 10.dp)){
        Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.SpaceBetween){
            Text("Pending", color = MaterialTheme.colorScheme.primary)
            LazyColumn(Modifier.weight(1f)) {
                items(vm.filterInvitationByStatus(Status.PENDING)) {
                    InvitationListItem(it)
                }
            }
            ListItemDivider()
        }
        Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.SpaceBetween){
            Text("Accepted", color = MaterialTheme.colorScheme.primary)
            LazyColumn(Modifier.weight(1f)) {
                items(vm.filterInvitationByStatus(Status.ACCEPTED)) {
                    InvitationListItem(it)
                }
            }
            ListItemDivider()
        }
        Column(Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.Top){
            Text("Rejected", color = MaterialTheme.colorScheme.primary)
            LazyColumn(Modifier.weight(1f)) {
                items(vm.filterInvitationByStatus(Status.REJECTED)) {
                    InvitationListItem(it)
                }
            }
        }
    }
}

@Composable
private fun InvitationListItem(invitation: Invitation, vm: InvitationViewModel = hiltViewModel()){
    val context = LocalContext.current
    val user = remember(vm.findUser(invitation.userId)){ mutableStateOf(vm.findUser(invitation.userId))}
    if(user.value != null){
        Row(Modifier.fillMaxWidth()
                    .padding(horizontal=5.dp, vertical=3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
            UserData(user.value!!)
            Icon(Icons.Default.Clear, contentDescription = "delete invitation", Modifier.clickable {
                vm.deleteInvitation(invitation._id!!, context)
            }.padding(horizontal = 10.dp, vertical = 15.dp))
        }
    }
}


@Composable
private fun UserData(user: UserInfo){
    Row(Modifier.padding(horizontal = 10.dp, vertical = 15.dp)){
        Box(
            Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
                .border(3.dp, Color.White, CircleShape)
        ) {
            CustomImage(Modifier.fillMaxSize(), user.profilePictureFilePath,ImageSourceSelector.PROFILE )
        }
        Column(Modifier.padding(start=10.dp)){
            Text(user.username, fontSize = 16.sp,color= MaterialTheme.colorScheme.primary)
            Text(user.name, Modifier.padding(start=5.dp), color= MaterialTheme.colorScheme.primary)
        }
    }
}