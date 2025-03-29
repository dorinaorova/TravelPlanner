package com.androidlab.travelplannerapp.data.service.ticket

import com.androidlab.travelplannerapp.data.model.Payment
import com.androidlab.travelplannerapp.data.model.Ticket
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface TicketService {
    @Headers("Accept: application/json")
    @GET("ticket/{id}")
    fun getById(@Path("id") id: String) : Call<Ticket>?

    @Headers("Accept: application/json")
    @GET("ticket/travel/{id}")
    fun getByTravelId(@Path("id") id: String) : Call<List<Ticket>>?

    @Headers("Accept: application/json")
    @POST("ticket")
    fun createTicket(@Body ticket: Ticket) : Call<Ticket>?

    @Headers("Accept: application/json")
    @Multipart
    @POST("ticket/upload/{id}")
    fun uploadTicketFile(@Part file: MultipartBody.Part, @Path("id") id: String) : Call<Ticket>?

    @Headers("Accept: application/pdf")
    @GET("ticket/download/{fileName}")
    fun downloadTicketFile(@Path("fileName") fileName: String) : Call<ResponseBody>?

}