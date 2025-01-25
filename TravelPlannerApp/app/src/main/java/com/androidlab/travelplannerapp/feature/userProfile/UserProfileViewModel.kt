package com.androidlab.travelplannerapp.feature.userProfile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {
    private var _user = mutableStateOf(UserInfo("","","","","","", "", emptyList(), "", ""))

    val user: UserInfo
        get() = _user.value

    fun logout(context: Context, navController: NavController){
        val sharedPreferences = context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        navController.navigate(Screen.LoginScreen.route){
            popUpTo(0)
        }
    }

    fun loadUserData(_id: String?, context: Context){
        viewModelScope.launch {
            var id = _id
            if (id == null) {
                val sharedPreferences =
                    context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
                id = sharedPreferences.getString("id", null)
            }
            val call = getUserDataUseCase(id!!)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                _user.value=response.body()!!
            }
        }
    }

    fun backgroundPicturePath(context: Context): String{
        val BASE_URL = context.getString(R.string.BASE_URL)
        return BASE_URL+"user/image/background/"+user.backgroundPictureFilePath
    }

    fun profilePictureFilePath(context: Context): String{
        val BASE_URL = context.getString(R.string.BASE_URL)
        return BASE_URL+"user/image/profile/"+user.profilePictureFilePath
    }

    fun ownProfile(id: String?, context: Context): Boolean{
        val sharedPreferences =
            context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        val savedId = sharedPreferences.getString("id", null)
        return id == null || id == savedId
    }
}