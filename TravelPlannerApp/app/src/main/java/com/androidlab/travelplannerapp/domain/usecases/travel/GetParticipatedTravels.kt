package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class GetParticipatedTravels @Inject constructor(private val travelRepository: TravelRepository) {
    operator fun invoke(id: String): Call<List<Travel>>? {
        return travelRepository.getParticipatedTravels(id)
    }
}