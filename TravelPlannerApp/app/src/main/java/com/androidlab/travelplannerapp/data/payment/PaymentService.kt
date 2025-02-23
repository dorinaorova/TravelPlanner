package com.androidlab.travelplannerapp.data.payment

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentService {
    @Headers("Accept: application/json")
    @GET("spends/travel/{id}")
    fun getAllByTravel(@Path("id") id: String) : Call<List<Payment>>?

    @Headers("Accept: application/json")
    @POST("spends")
    fun addSpend(@Body spend: Payment) : Call<Payment>?

    @Headers("Accept: application/json")
    @DELETE("spends/{id}")
    fun deleteSpend(@Path("id") id: String) : Call<Void>?

    @Headers("Accept: application/json")
    @GET("spends/transaction/{id}")
    fun getTransactions(@Path("id") id: String) : Call<List<Transaction>>?
}