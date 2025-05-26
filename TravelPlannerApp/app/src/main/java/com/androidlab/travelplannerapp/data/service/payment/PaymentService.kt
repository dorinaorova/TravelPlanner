package com.androidlab.travelplannerapp.data.service.payment

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Transaction
import com.androidlab.travelplannerapp.data.repository.PaymentRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentService :PaymentRepository{
    @Headers("Accept: application/json")
    @GET("spends/travel/{id}")
    override fun getAllByTravel(@Path("id") id: String) : Call<List<Payment>>?

    @Headers("Accept: application/json")
    @POST("spends")
    override fun addSpend(@Body spend: Payment) : Call<Payment>?

    @Headers("Accept: application/json")
    @DELETE("spends/{id}")
    override fun deleteSpend(@Path("id") id: String) : Call<Void>?

    @Headers("Accept: application/json")
    @GET("spends/transaction/{id}")
    override fun getTransactions(@Path("id") id: String) : Call<List<Transaction>>?
}