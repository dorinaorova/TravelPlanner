package com.androidlab.travelplannerapp.feature.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.domain.usecases.auth.CheckRefreshTokenUseCase
import com.androidlab.travelplannerapp.domain.usecases.auth.SignInUseCase
import com.androidlab.travelplannerapp.navigation.Screen
import com.androidlab.travelplannerapp.sharedUtils.getRefreshToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val checkRefreshTokenUseCase: CheckRefreshTokenUseCase
): ViewModel() {

    fun checkRefreshToken(context: Context, navController: NavController){
        viewModelScope.launch {
            val refreshToken = getRefreshToken(context)
           if(refreshToken != null){
               val call = checkRefreshTokenUseCase(refreshToken)
               val isRefreshTokenExpired = call?.awaitResponse()!!.body() as Boolean
               if(isRefreshTokenExpired){
                    navController.navigate(route = Screen.HomeScreen.route)
               }
           }
        }
    }

    fun login(username: String, password: String, context: Context, navController: NavController){
        viewModelScope.launch {
                val call = signInUseCase(username, password)
                val response = call?.awaitResponse()
                if(response?.isSuccessful == true){
                    val token = response.body()!!.jwt
                    val refreshToken = response.body()!!.refreshToken
                    var sharedPref : SharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF",MODE_PRIVATE)
                    var editor : SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("jwt_token", token).apply()
                    editor.putString("refresh_token", refreshToken).apply()
                    navController.navigate(route = Screen.HomeScreen.route)
                }else{
                    Toast.makeText(context, "Login failed\n" +
                            "${response?.code()} - ${response?.message()}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}