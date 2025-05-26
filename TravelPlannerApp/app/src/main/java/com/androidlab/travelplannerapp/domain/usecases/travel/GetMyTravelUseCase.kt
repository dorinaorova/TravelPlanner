package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import retrofit2.Call
import javax.inject.Inject

class GetMyTravelUseCase @Inject constructor(private val travelRepository: TravelRepository) {
    operator fun invoke(id: String): Call<List<Travel>>? {
        return travelRepository.getMyTravel(id)
    }
}