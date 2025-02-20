package com.androidlab.travelplannerapp.domain.module.api

import android.content.Context
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.auth.AuthService
import com.androidlab.travelplannerapp.data.image.ImageService
import com.androidlab.travelplannerapp.data.interceptor.AuthInterceptor
import com.androidlab.travelplannerapp.data.interceptor.TokenInterceptor
import com.androidlab.travelplannerapp.data.invitation.InvitationService
import com.androidlab.travelplannerapp.data.travel.TravelService
import com.androidlab.travelplannerapp.data.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiUseCaseModule {
    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val BASE_URL = context.getString(R.string.BASE_URL)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies
        val authInterceptor = AuthInterceptor(context)
        val tokenInterceptor = TokenInterceptor(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideTravelService(retrofit: Retrofit): TravelService {
        return retrofit.create(TravelService::class.java)
    }
    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageService(retrofit: Retrofit): ImageService {
        return retrofit.create(ImageService::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationService(retrofit: Retrofit) : InvitationService{
        return retrofit.create(InvitationService::class.java)
    }

}