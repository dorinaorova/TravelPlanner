package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import javax.inject.Inject

class UpdateTravelUseCase @Inject constructor(
    private val travelRepository: TravelRepository
) {
    operator fun invoke(travel: Travel, id: String) = travelRepository.updateTravel(travel, id)
}