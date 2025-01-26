package com.androidlab.travelplannerapp.feature.userProfile.userUpdate


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class UserUpdateViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel(){
    private var _user = mutableStateOf(UserInfo("","","","","","", "",emptyList(), "", "", emptyList(), emptyList()))
    private var _id = mutableStateOf("")

    val user: UserInfo
        get() = _user.value

    fun fetchData(context: Context){
        viewModelScope.launch {
            val sharedPreferences =
                context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
            _id.value = sharedPreferences.getString("id", null)!!
            val call = getUserDataUseCase(_id.value)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _user.value=response.body()!!
            }
        }
    }

    fun updateUser(name: String, email: String, description: String?, city: String?, country: String?, navController: NavController){
        viewModelScope.launch {
            val updateUser =
                UserInfo("", "", name, email, description, "", "",emptyList(), city, country, emptyList(), emptyList())
            val call = updateUserUseCase(_id.value, updateUser)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                navController.navigate("profile")
            }
        }
    }
}