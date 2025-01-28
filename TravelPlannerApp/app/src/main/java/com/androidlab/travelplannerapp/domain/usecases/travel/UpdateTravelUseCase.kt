package com.androidlab.travelplannerapp.domain.usecases.travel

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.travel.TravelService
import javax.inject.Inject

class UpdateTravelUseCase @Inject constructor(
    private val travelService: TravelService
) {
    operator fun invoke(travel: Travel) = travelService.updateTravel(travel)
}