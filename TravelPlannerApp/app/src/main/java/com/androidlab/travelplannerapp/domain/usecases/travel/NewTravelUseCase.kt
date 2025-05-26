package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import retrofit2.Call
import javax.inject.Inject

class NewTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    operator fun invoke(id: String, travel: Travel): Call<Travel>? {
        return travelRepository.newTravel(id, travel)
    }
}