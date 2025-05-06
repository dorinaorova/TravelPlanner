package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class GetTravelByUserIdUseCase @Inject constructor(private val travelService: TravelService) {
    operator fun invoke(id: String): Call<List<Travel>>? {
        return travelService.getTravelByUserId(id)
    }
}