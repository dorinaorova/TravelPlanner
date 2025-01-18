package com.androidlab.travelplannerapp.feature.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.domain.usecases.auth.SingUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private  val signUpUseCase: SingUpUseCase
): ViewModel() {

    fun signUp(username: String, password: String,name: String, email: String){
        viewModelScope.launch {
            try{
                val call = signUpUseCase(username, password, email, name)
                val response = call?.awaitResponse()
                if(response?.isSuccessful == true){
                    Log.d("LoginViewModel", "Login result: ${response.body()}")
                }else{
                    Log.e("LoginViewModel", "Login failed: ${response?.code()} - ${response?.message()}")
                    throw error(message = "Login failed")
                }
            }catch (e: Exception){
                //TODO
            }
        }

    }
    fun checkEmailFormat(email: String): Boolean{
        val emailPattern: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.isEmpty() || email.matches(emailPattern)
    }

}