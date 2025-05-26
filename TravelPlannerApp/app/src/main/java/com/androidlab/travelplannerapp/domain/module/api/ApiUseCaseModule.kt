package com.androidlab.travelplannerapp.domain.module.api

import android.content.Context
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.service.image.ImageService
import com.androidlab.travelplannerapp.data.interceptor.AuthInterceptor
import com.androidlab.travelplannerapp.data.interceptor.TokenInterceptor
import com.androidlab.travelplannerapp.data.repository.ActivityRepository
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import com.androidlab.travelplannerapp.data.repository.ImageRepository
import com.androidlab.travelplannerapp.data.repository.InvitationRepository
import com.androidlab.travelplannerapp.data.repository.PaymentRepository
import com.androidlab.travelplannerapp.data.repository.TicketRepository
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import com.androidlab.travelplannerapp.data.repository.UserRepository
import com.androidlab.travelplannerapp.data.service.invitation.InvitationService
import com.androidlab.travelplannerapp.data.service.payment.PaymentService
import com.androidlab.travelplannerapp.data.service.ticket.TicketService
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import com.androidlab.travelplannerapp.data.service.user.UserService
import com.androidlab.travelplannerapp.data.service.activities.ActivityService
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
    fun provideAuthRepository(retrofit: Retrofit): AuthRepository {
        return retrofit.create(AuthService::class.java)
    }


    @Provides
    @Singleton
    fun provideTravelRepository(retrofit: Retrofit): TravelRepository {
        return retrofit.create(TravelService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(retrofit: Retrofit): UserRepository {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRepository(retrofit: Retrofit): ImageRepository {
        return retrofit.create(ImageService::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationRepository(retrofit: Retrofit) : InvitationRepository {
        return retrofit.create(InvitationService::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(retrofit: Retrofit): PaymentRepository {
        return retrofit.create(PaymentService::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketRepository(retrofit: Retrofit): TicketRepository {
        return retrofit.create(TicketService::class.java)
    }
        @Provides
    @Singleton
    fun provideActivityRepository(retrofit: Retrofit): ActivityRepository {
        return retrofit.create(ActivityService::class.java)
    }
}