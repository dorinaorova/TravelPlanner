package com.androidlab.travelplannerapp.feature.travelCreate

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.androidlab.travelplannerapp.data.model.Travel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TravelCreateUpdateViewModel @Inject constructor(): ViewModel(){
    private var _travel = mutableStateOf(Travel("", "", 0L, 0L, "", "", "0", null, null, null))

    val travel: Travel
        get() = _travel.value
}