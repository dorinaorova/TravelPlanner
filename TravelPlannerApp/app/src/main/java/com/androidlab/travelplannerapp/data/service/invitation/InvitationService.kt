package com.androidlab.travelplannerapp.data.service.invitation

import com.androidlab.travelplannerapp.data.model.Invitation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface InvitationService {
    @Headers("Accept: application/json")
    @GET("invitation/travel/{id}")
    fun getByTravelId(@Path("id") id: String) : Call<List<Invitation>>?

    @Headers("Accept: application/json")
    @GET("invitation/user/{id}")
    fun getByUserId(@Path("id") id: String) : Call<List<Invitation>>?

    @Headers("Accept: application/json")
    @POST("invitation")
    fun createInvitation(@Body invitation: Invitation) : Call<Invitation>?

    @Headers("Accept: application/json")
    @GET("invitation/accept/{id}")
    fun accept(@Path("id") id: String) : Call<Void>

    @Headers("Accept: application/json")
    @GET("invitation/reject/{id}")
    fun reject(@Path("id") id: String) : Call<Void>

    @Headers("Accept: application/json")
    @DELETE("invitation/{id}")
    fun delete(@Path("id") id: String) : Call<Void>
}