package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.Invitation
import retrofit2.Call
import retrofit2.http.Body

interface InvitationRepository {
    fun getByTravelId(id: String) : Call<List<Invitation>>?

    fun getByUserId(id: String) : Call<List<Invitation>>?

    fun createInvitation(invitation: Invitation) : Call<Invitation>?

    fun accept(id: String) : Call<Void>

    fun reject(id: String) : Call<Void>

    fun delete(id: String) : Call<Void>
}