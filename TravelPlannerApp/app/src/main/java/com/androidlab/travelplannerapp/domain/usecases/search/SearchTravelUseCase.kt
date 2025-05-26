package com.androidlab.travelplannerapp.domain.usecases.search

import com.androidlab.travelplannerapp.data.model.Travel
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import retrofit2.Call
import javax.inject.Inject

class SearchTravelUseCase @Inject constructor(private val travelRepository: TravelRepository) {
    operator fun invoke(name: String? = null, city: String? = null, country: String? = null, tags: List<String>? = null, minDays:Int? = null, maxDays: Int? =null, minPrice:Int? = null, maxPrice: Int? = null): Call<List<Travel>>? {
        return travelRepository.getAll(name, city, country, tags, minDays, maxDays, minPrice, maxPrice)
    }
}