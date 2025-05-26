package com.androidlab.travelplannerapp

import android.content.Context
import com.androidlab.travelplannerapp.data.repository.ActivityRepository
import com.androidlab.travelplannerapp.data.repository.AuthRepository
import com.androidlab.travelplannerapp.data.repository.ImageRepository
import com.androidlab.travelplannerapp.data.repository.InvitationRepository
import com.androidlab.travelplannerapp.data.repository.PaymentRepository
import com.androidlab.travelplannerapp.data.repository.TicketRepository
import com.androidlab.travelplannerapp.data.repository.TravelRepository
import com.androidlab.travelplannerapp.data.repository.UserRepository
import com.androidlab.travelplannerapp.data.service.activities.ActivityService
import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.service.image.ImageService
import com.androidlab.travelplannerapp.data.service.invitation.InvitationService
import com.androidlab.travelplannerapp.data.service.payment.PaymentService
import com.androidlab.travelplannerapp.data.service.ticket.TicketService
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import com.androidlab.travelplannerapp.data.service.user.UserService
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiUseCaseModule::class] // real module you want to override
)
object TestApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val baseUrl = TestConfig.mockBaseUrl
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideImageRepository(retrofit: Retrofit): ImageRepository {
        return retrofit.create(ImageService::class.java)
    }


    @Provides
    @Singleton
    fun provideUserRepository(retrofit: Retrofit): UserRepository {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(retrofit: Retrofit): AuthRepository {
        return retrofit.create(AuthService::class.java)
    }


    @Provides
    @Singleton
    fun provideInvitationRepository(retrofit: Retrofit): InvitationRepository {
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
    fun provideTravelRepository(retrofit: Retrofit): TravelRepository {
        return retrofit.create(TravelService::class.java)
    }

    @Provides
    @Singleton
    fun provideActivityRepository(retrofit: Retrofit): ActivityRepository {
        return retrofit.create(ActivityService::class.java)
    }
}

object TestConfig {
    var mockBaseUrl: String = "http://localhost:8080"
}