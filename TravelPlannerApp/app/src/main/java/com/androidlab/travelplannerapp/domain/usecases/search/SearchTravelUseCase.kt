package com.androidlab.travelplannerapp.domain.usecases.search

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class SearchTravelUseCase @Inject constructor(private val travelService: TravelService) {
    operator fun invoke(): Call<List<Travel>>? {
        return travelService.getAll()
    }
}