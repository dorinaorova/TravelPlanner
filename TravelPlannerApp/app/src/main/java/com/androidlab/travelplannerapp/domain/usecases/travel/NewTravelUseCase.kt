package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class NewTravelUseCase @Inject constructor(
    private val travelService: TravelService
) {
    operator fun invoke(id: String, travel: Travel): Call<Travel>? {
        return travelService.newTravel(id, travel)
    }
}