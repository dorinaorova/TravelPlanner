package com.androidlab.travelplannerapp.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.domain.usecases.auth.SignInUseCase
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    fun login(username: String, password: String){
        viewModelScope.launch {
            try{
                val result = signInUseCase(username, password)
                Log.d("LoginViewModel", "Login result: $result")
            }catch (e: Exception){
                //TODO
            }
        }
    }
}