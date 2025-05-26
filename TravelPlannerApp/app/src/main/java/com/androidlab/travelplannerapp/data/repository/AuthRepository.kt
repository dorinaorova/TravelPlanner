package com.androidlab.travelplannerapp.data.repository

import com.androidlab.travelplannerapp.data.model.LoginRequest
import com.androidlab.travelplannerapp.data.model.LoginResponse
import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import com.androidlab.travelplannerapp.data.model.SignUpRequest
import com.androidlab.travelplannerapp.data.model.UserInfo
import retrofit2.Call

interface AuthRepository {

    fun login(user: LoginRequest) : Call<LoginResponse>?

    fun singUp(user: SignUpRequest) : Call<UserInfo>?

    fun checkRefreshToken(token: RefreshTokenRequest) : Call<Boolean>?
}