package com.androidlab.travelplannerapp.data.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(chain.request().url.encodedPath.contains("login") || chain.request().url.encodedPath.contains("register")){
            return chain.proceed(chain.request())
        }
        else{
            val sharedPreferences = context.applicationContext.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("jwt_token", null)

            if (token != null) {
                return chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                )
            } else {
                throw Exception("TOKEN NOT FOUND")
            }
        }
    }
}