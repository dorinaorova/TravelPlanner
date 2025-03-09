package com.androidlab.travelplannerapp.domain.usecases.payment

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Transaction
import com.androidlab.travelplannerapp.data.service.payment.PaymentService
import retrofit2.Call
import javax.inject.Inject

class GetPaymentsByTravelIdUseCase @Inject constructor(private val paymentService: PaymentService) {
    operator fun invoke(id: String): Call<List<Payment>>? {
        return paymentService.getAllByTravel(id)
    }
}

class AddPaymentUseCase @Inject constructor(private val paymentService: PaymentService) {
    operator fun invoke(payment: Payment): Call<Payment>? {
        return paymentService.addSpend(payment)
    }
}

class DeletePaymentUseCase @Inject constructor(private val paymentService: PaymentService) {
    operator fun invoke(id: String): Call<*>? {
        return paymentService.deleteSpend(id)
    }
}

class GetTransactionsUseCase @Inject constructor(private val paymentService: PaymentService) {
    operator fun invoke(id: String): Call<List<Transaction>>? {
        return paymentService.getTransactions(id)
    }

}