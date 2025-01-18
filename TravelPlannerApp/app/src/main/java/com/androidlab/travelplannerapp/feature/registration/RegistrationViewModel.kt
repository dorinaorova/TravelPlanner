package com.androidlab.travelplannerapp.feature.registration

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androidlab.travelplannerapp.domain.usecases.auth.SingUpUseCase
import com.androidlab.travelplannerapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private  val signUpUseCase: SingUpUseCase
): ViewModel() {

    fun signUp(username: String, password: String, name: String, email: String, context: Context, navController: NavController){
        viewModelScope.launch {
                val call = signUpUseCase(username, password, email, name)
                val response = call?.awaitResponse()
                if(response?.isSuccessful == true){
                    Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                    navController.navigate(route = Screen.LoginScreen.route)
                }else{
                    Toast.makeText(context, "Failed!\n${response?.code()} - ${response?.message()}", Toast.LENGTH_SHORT).show()
                }
        }

    }
    fun checkEmailFormat(email: String): Boolean{
        val emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.isEmpty() || email.matches(emailPattern)
    }

}