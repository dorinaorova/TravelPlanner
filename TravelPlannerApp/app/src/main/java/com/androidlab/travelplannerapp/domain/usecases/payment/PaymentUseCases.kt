package com.androidlab.travelplannerapp.domain.usecases.payment

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Transaction
import com.androidlab.travelplannerapp.data.repository.PaymentRepository
import retrofit2.Call
import javax.inject.Inject

class GetPaymentsByTravelIdUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    operator fun invoke(id: String): Call<List<Payment>>? {
        return paymentRepository.getAllByTravel(id)
    }
}

class AddPaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    operator fun invoke(payment: Payment): Call<Payment>? {
        return paymentRepository.addSpend(payment)
    }
}

class DeletePaymentUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    operator fun invoke(id: String): Call<*>? {
        return paymentRepository.deleteSpend(id)
    }
}

class GetTransactionsUseCase @Inject constructor(private val paymentRepository: PaymentRepository) {
    operator fun invoke(id: String): Call<List<Transaction>>? {
        return paymentRepository.getTransactions(id)
    }

}