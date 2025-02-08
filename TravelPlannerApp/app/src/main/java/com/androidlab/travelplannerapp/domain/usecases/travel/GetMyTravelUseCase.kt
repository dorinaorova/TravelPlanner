package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class GetMyTravelUseCase @Inject constructor(private val travelService: TravelService) {
    operator fun invoke(id: String): Call<List<Travel>>? {
        return travelService.getMyTravel(id)
    }
}