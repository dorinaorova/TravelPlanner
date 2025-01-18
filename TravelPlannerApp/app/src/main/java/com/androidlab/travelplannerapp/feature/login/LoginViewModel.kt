package com.androidlab.travelplannerapp.feature.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.domain.usecases.auth.SignInUseCase
import com.androidlab.travelplannerapp.navigation.Screen
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
): ViewModel() {

    fun login(username: String, password: String, context: Context, navController: NavController){
        viewModelScope.launch {
                val call = signInUseCase(username, password)
                val response = call?.awaitResponse()
                if(response?.isSuccessful == true){
                    navController.navigate(route = Screen.HomeScreen.route)
                }else{
                    Toast.makeText(context, "Login failed\n" +
                            "${response?.code()} - ${response?.message()}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}