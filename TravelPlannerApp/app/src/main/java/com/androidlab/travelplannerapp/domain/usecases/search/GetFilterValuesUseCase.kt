package com.androidlab.travelplannerapp.domain.usecases.search

import com.androidlab.travelplannerapp.data.travel.TravelService
import retrofit2.Call
import javax.inject.Inject

class GetFilterValuesUseCase @Inject constructor(private val travelService: TravelService) {
    operator fun invoke(): Call<List<Int>>? {
        return travelService.getFilterValues()
    }
}