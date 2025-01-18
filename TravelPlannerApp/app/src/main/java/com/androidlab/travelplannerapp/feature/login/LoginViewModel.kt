package com.androidlab.travelplannerapp.feature.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.domain.usecases.auth.SignInUseCase
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    fun login(username: String, password: String){
        viewModelScope.launch {
            try{
                val call = signInUseCase(username, password)
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
}