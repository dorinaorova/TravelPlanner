package com.androidlab.travelplannerapp.feature.vacation.payment

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.SpendType
import com.androidlab.travelplannerapp.data.model.Transaction
import com.androidlab.travelplannerapp.data.model.UserInfo
import com.androidlab.travelplannerapp.domain.usecases.payment.AddPaymentUseCase
import com.androidlab.travelplannerapp.domain.usecases.payment.DeletePaymentUseCase
import com.androidlab.travelplannerapp.domain.usecases.payment.GetPaymentsByTravelIdUseCase
import com.androidlab.travelplannerapp.domain.usecases.payment.GetTransactionsUseCase
import com.androidlab.travelplannerapp.domain.usecases.search.SearchUserUseCase
import com.androidlab.travelplannerapp.domain.usecases.travel.GetTravelByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel@Inject constructor(
    private val getPaymentsUseCase: GetPaymentsByTravelIdUseCase,
    private val addPaymentUseCase: AddPaymentUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val getTravelByIdUseCase: GetTravelByIdUseCase,
) : ViewModel() {
    private val _payments = mutableStateOf<List<Payment>>(emptyList())
    private val _transactions = mutableStateOf<List<Transaction>>(emptyList())
    private val  _users = mutableStateOf<List<UserInfo>>(emptyList())
    private val _participants = mutableStateOf<List<UserInfo>>(emptyList())
    private var travelId = ""

    val payments: List<Payment>
        get() {
            return _payments.value
        }
    val transactions: List<Transaction>
        get(){
            return _transactions.value
    }

    val participants: List<UserInfo>
        get(){
            return _participants.value
        }

    fun fetchData(id: String){
        viewModelScope.launch{
            travelId = id
            _users.value = emptyList()
            _payments.value = emptyList()
            _transactions.value = emptyList()
            _participants.value= emptyList()

            val paymentCall = getPaymentsUseCase(travelId)
            val paymentResponse = paymentCall?.awaitResponse()
            if(paymentResponse!!.isSuccessful){
                _payments.value = paymentResponse.body()!!.sortedByDescending { it.date }
            }

            val transactionCall = getTransactionsUseCase(travelId)
            val transactionResponse = transactionCall?.awaitResponse()
            if(transactionResponse!!.isSuccessful){
                _transactions.value = transactionResponse.body()!!
            }

            val call = searchUserUseCase("")
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                _users.value = response.body()!!
            }

            val travelCall = getTravelByIdUseCase(travelId)
            val travelResponse = travelCall?.awaitResponse()
            if(travelResponse?.isSuccessful == true){
                val travel = travelResponse.body()!!
                _participants.value= _users.value.filter { user -> user._id in travel.participantIds!! || user._id == travel.ownerId }
            }
        }
    }

    fun findUser(id: String): String{
        for(user in _users.value){
            if(user._id == id){
                return user.username
            }
        }
        return "?"
    }


    fun addPayment(from: String, to: List<String>, cost: Double, type: String){
        viewModelScope.launch {
            val payment = Payment(userId = from, partUserIds = to, cost = cost, type = SpendType.valueOf(type), date = System.currentTimeMillis(), _id = null, travelId = travelId)
            val call = addPaymentUseCase(payment)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                fetchData(travelId)
            }

        }
    }

    fun calculateDebt(): Map<String, Double>{
        val debts = mutableMapOf<String, Double>()
        val userIds = _payments.value.flatMap { it.partUserIds + it.userId }.toSet()
        userIds.forEach{id ->
            var debt = 0.0
            _payments.value.forEach{
                if(it.userId == id){
                    debt+=it.cost
                }
                if(it.partUserIds.contains(id)){
                    debt-=it.cost/it.partUserIds.size
                }
            }
            debts[id] = debt
        }
        return debts
    }

    fun calculateSum(): Double{
        var sum = 0.0
        _payments.value.forEach{
            sum+=it.cost
        }
        return sum
    }

    fun settleDebt(transaction: Transaction){
        viewModelScope.launch {
            val payment = Payment(userId = transaction.fromUser, partUserIds = listOf(transaction.toUser), cost = transaction.amount, type = SpendType.SETTLEMENT, date = System.currentTimeMillis(), _id = null, travelId = travelId)
            val call = addPaymentUseCase(payment)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                fetchData(travelId)
            }
        }
    }

    fun deletePayment(id: String){
        viewModelScope.launch {
            val call = deletePaymentUseCase(id)
            val response = call?.awaitResponse()
            if(response?.isSuccessful == true){
                fetchData(travelId)
            }
        }
    }
}