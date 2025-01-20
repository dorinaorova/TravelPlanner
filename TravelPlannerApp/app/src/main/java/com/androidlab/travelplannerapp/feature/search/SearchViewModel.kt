package com.androidlab.travelplannerapp.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.domain.usecases.search.SearchTravelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor( private val searchTravelUseCase: SearchTravelUseCase )  : ViewModel() {

    fun getAllTravel(){
        viewModelScope.launch{
            val call = searchTravelUseCase()
            val response = call?.awaitResponse()
            Log.d("RESPONSE", response!!.code().toString())
        }
    }
}