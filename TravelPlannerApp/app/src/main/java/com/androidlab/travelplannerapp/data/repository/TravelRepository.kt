package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Travel
import retrofit2.Call
interface TravelRepository {
    fun getAll(name: String? = null,
               city: String? = null,
               country: String? = null,
               tags: List<String>? = null,
               minDays:Int? = null,
               maxDays: Int? =null,
               minPrice:Int? = null,
               maxPrice: Int? = null) : Call<List<Travel>>?

    fun getById(id: String) : Call<Travel>?

    fun getTravelByUserId(id: String) : Call<List<Travel>>?

    fun newTravel(id: String, travel: Travel) : Call<Travel>?

    fun updateTravel(travel: Travel, id: String) : Call<Travel>?

    fun getFilterValues() : Call<List<Int>>?

    fun getMyTravel( id: String): Call<List<Travel>>?

    fun getParticipatedTravels(id: String): Call<List<Travel>>?

}