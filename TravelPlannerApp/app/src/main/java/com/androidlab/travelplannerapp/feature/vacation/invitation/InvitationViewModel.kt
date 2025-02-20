package com.androidlab.travelplannerapp.feature.vacation.invitation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.model.Status
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.invitation.CreateInvitationUseCase
import com.androidlab.travelplannerapp.domain.usecases.invitation.DeleteInvitationUseCase
import com.androidlab.travelplannerapp.domain.usecases.invitation.GetInvitationsByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import com.androidlab.travelplannerapp.feature.utils.getOwnUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val getInvitationsByTravelIdUseCase: GetInvitationsByTravelIdUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val createInvitationUseCase: CreateInvitationUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase
) : ViewModel() {
    var travelId : String = ""
    private val _invitations = mutableStateOf<List<Invitation>>(emptyList())
    private val _users = mutableStateOf<List<UserInfo>>(emptyList())
    private val  _filteredUsers = mutableStateOf<List<UserInfo>>(emptyList())

    val users: List<UserInfo>
        get(){
            return _filteredUsers.value
        }

    fun fetchData(id: String? = travelId){
        viewModelScope.launch {
            _invitations.value = emptyList()
            val call = getInvitationsByTravelIdUseCase(id!!)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true){
                _invitations.value = response.body()!!
            }
        }
    }

    fun filterInvitationByStatus(status: Status) : List<Invitation> {
        if (_invitations.value.isEmpty()) {
            return emptyList()

        }
        return when (status) {
            Status.PENDING -> {
                _invitations.value.filter { it.status == Status.PENDING }
            }

            Status.ACCEPTED -> {
                _invitations.value.filter { it.status == Status.ACCEPTED }
            }

            Status.REJECTED -> {
                _invitations.value.filter { it.status == Status.REJECTED }
            }
        }
    }

    fun fetchUsers(context: Context){
        viewModelScope.launch {
            _users.value = emptyList()
            val call = searchUserUseCase("")
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _users.value = response.body()!!
                _filteredUsers.value = response.body()!!.filter { user -> user._id != getOwnUserId(context) && _invitations.value.firstOrNull({it.userId == user._id}) == null }
            }
        }
    }


    fun findUser(id: String): UserInfo? {
        return _users.value.firstOrNull { it._id == id }
    }

    fun inviteUser(user: UserInfo){
        viewModelScope.launch {
            val call = createInvitationUseCase(Invitation(null,user._id!!, travelId ))
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                fetchData()
            }
        }
    }

    fun deleteInvitation(id: String){
        viewModelScope.launch {
            val call = deleteInvitationUseCase(id)
            val response = call.awaitResponse()
            if(response.isSuccessful){
                fetchData()
            }
        }
    }
}