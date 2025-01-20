package com.androidlab.travelplannerapp.data.interceptor

import android.content.Context
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.model.RefreshTokenRequest
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

class TokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(chain.request().url.encodedPath.contains("auth")){
            return chain.proceed(chain.request())
        }else{
            val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
            val refreshToken = sharedPreferences.getString("refresh_token", null)
            val request: Request = chain.request()
            val response = chain.proceed(request)
            if(response.code == 401){
                if (refreshToken != null){
                    val token = refreshApiCall(refreshToken)
                    response.close()
                    if(token != null){
                        val newRequest = request.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                        return chain.proceed(newRequest)
                    }
                }

            }
            return response
        }
    }

    fun saveAccessToken(accessToken: String) {
        val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("jwt_token", accessToken).apply()
    }

    fun saveRefreshToken(accessToken: String) {
        val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("refresh_token", accessToken).apply()
    }

    fun refreshApiCall(refreshToken: String): String? {
        val BASE_URL = context.getString(R.string.BASE_URL)
        val url = BASE_URL+"auth/refresh-token"

        val gson = Gson()
        val jsonRequest = gson.toJson(RefreshTokenRequest(refreshToken))

        val client = OkHttpClient()
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonRequest
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body!!.string())
                val newAccessToken = jsonResponse.getString("jwt")
                val newRefreshToken = jsonResponse.getString("refreshToken")
                saveAccessToken(newAccessToken)
                saveRefreshToken(newRefreshToken)
                return newAccessToken
            }
        }
        return null
    }
}


