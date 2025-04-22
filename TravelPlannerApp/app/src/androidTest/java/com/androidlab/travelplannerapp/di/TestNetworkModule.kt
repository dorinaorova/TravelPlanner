package com.androidlab.travelplannerapp.di

import android.content.Context
import androidx.test.espresso.core.internal.deps.dagger.Module
import androidx.test.espresso.core.internal.deps.dagger.Provides
import com.androidlab.travelplannerapp.R
import com.androidlab.travelplannerapp.domain.module.api.ApiUseCaseModule
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.internal.TestSingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@ExperimentalCoroutinesApi
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [ApiUseCaseModule::class]
//)
//object TestNetworkModule {
//
//    @Provides
//    @Singleton
//    fun provideBaseUrl(@ApplicationContext context: Context): String {
//        return context.getString(R.string.BASE_URL)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(
//        @ApplicationContext context: Context
//    ): Retrofit {
//        val baseUrl = context.getString(R.string.BASE_URL)
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}