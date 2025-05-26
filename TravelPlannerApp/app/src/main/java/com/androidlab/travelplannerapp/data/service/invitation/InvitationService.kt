package com.androidlab.travelplannerapp.data.service.invitation

import com.androidlab.travelplannerapp.data.model.Invitation
import com.androidlab.travelplannerapp.data.repository.InvitationRepository
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface InvitationService: InvitationRepository {
    @Headers("Accept: application/json")
    @GET("invitation/travel/{id}")
    override fun getByTravelId(@Path("id") id: String) : Call<List<Invitation>>?

    @Headers("Accept: application/json")
    @GET("invitation/user/{id}")
    override fun getByUserId(@Path("id") id: String) : Call<List<Invitation>>?

    @Headers("Accept: application/json")
    @POST("invitation")
    override fun createInvitation(@Body invitation: Invitation) : Call<Invitation>?

    @Headers("Accept: application/json")
    @GET("invitation/accept/{id}")
    override fun accept(@Path("id") id: String) : Call<Void>

    @Headers("Accept: application/json")
    @GET("invitation/reject/{id}")
    override fun reject(@Path("id") id: String) : Call<Void>

    @Headers("Accept: application/json")
    @DELETE("invitation/{id}")
    override fun delete(@Path("id") id: String) : Call<Void>
}