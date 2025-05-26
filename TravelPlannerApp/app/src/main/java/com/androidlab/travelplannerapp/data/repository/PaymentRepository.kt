package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Transaction
import retrofit2.Call

interface PaymentRepository {
    fun getAllByTravel(id: String) : Call<List<Payment>>?

    fun addSpend(spend: Payment) : Call<Payment>?

    fun deleteSpend(id: String) : Call<Void>?

    fun getTransactions(id: String) : Call<List<Transaction>>?
}