package com.androidlab.travelplannerapp

import android.content.Context
import com.androidlab.travelplannerapp.data.activities.ActivityService
import com.androidlab.travelplannerapp.data.service.auth.AuthService
import com.androidlab.travelplannerapp.data.service.image.ImageService
import com.androidlab.travelplannerapp.data.service.invitation.InvitationService
import com.androidlab.travelplannerapp.data.service.payment.PaymentService
import com.androidlab.travelplannerapp.data.service.ticket.TicketService
import com.androidlab.travelplannerapp.data.service.travel.TravelService
import com.androidlab.travelplannerapp.data.service.user.UserService
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import com.androidlab.travelplannerapp.testServices.MockActivityService
import com.androidlab.travelplannerapp.testServices.MockAuthService
import com.androidlab.travelplannerapp.testServices.MockImageService
import com.androidlab.travelplannerapp.testServices.MockInvitationService
import com.androidlab.travelplannerapp.testServices.MockPaymentService
import com.androidlab.travelplannerapp.testServices.MockTicketService
import com.androidlab.travelplannerapp.testServices.MockTravelService
import com.androidlab.travelplannerapp.testServices.MockUserService
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
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        return  context.getString(R.string.BASE_URL)
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val baseUrl = context.getString(R.string.BASE_URL)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideImageService(): ImageService {
        return MockImageService()
    }


    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }


    @Provides
    @Singleton
    fun provideInvitationService(retrofit: Retrofit):InvitationService {
        return retrofit.create(InvitationService::class.java)
    }


    @Provides
    @Singleton
    fun providePaymentService(retrofit: Retrofit): PaymentService {
        return retrofit.create(PaymentService::class.java)
    }


    @Provides
    @Singleton
    fun provideTicketService(retrofit: Retrofit): TicketService {
        return retrofit.create(TicketService::class.java)
    }


    @Provides
    @Singleton
    fun provideTravelService(retrofit: Retrofit): TravelService {
        return retrofit.create(TravelService::class.java)
    }

    @Provides
    @Singleton
    fun provideActivityService(retrofit: Retrofit): ActivityService {
        return retrofit.create(ActivityService::class.java)
    }
}