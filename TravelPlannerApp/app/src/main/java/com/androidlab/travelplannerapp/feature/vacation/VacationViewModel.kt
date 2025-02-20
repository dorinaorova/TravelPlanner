package com.androidlab.travelplannerapp.feature.vacation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.user.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class VacationViewModel @Inject constructor(
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
    private val getUserByIdUseCase: GetUserDataUseCase
) : ViewModel() {
    private var _travel = mutableStateOf(Travel())
    private val _participants = mutableStateOf<List<UserInfo>>(emptyList())
    val travel : Travel
        get(){
            return _travel.value
        }
    val participants : List<UserInfo>
        get(){
            return _participants.value
        }

    fun fetchData(id: String){
        viewModelScope.launch {
            val call = getTravelByIdUseCase(id)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true){
                _travel.value = response.body()!!
                if(!_travel.value.participantIds.isNullOrEmpty()){
                    _participants.value = emptyList()
                    for(id in _travel.value.participantIds!!){
                        val userCall = getUserByIdUseCase(id)
                        val user = userCall?.awaitResponse()
                        if(user?.isSuccessful == true){
                            _participants.value += user.body()!!
                        }
                    }
                }

            }
        }
    }
}