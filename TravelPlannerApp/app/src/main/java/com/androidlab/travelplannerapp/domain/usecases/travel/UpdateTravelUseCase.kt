package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import javax.inject.Inject

class UpdateTravelUseCase @Inject constructor(
    private val travelService: TravelService
) {
    operator fun invoke(travel: Travel, id: String) = travelService.updateTravel(travel, id)
}