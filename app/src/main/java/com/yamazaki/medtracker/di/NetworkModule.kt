package com.yamazaki.medtracker.di

import com.yamazaki.medtracker.data.remote.api.OpenFdaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(
        json: Json
    ): Converter.Factory {
        return json.asConverterFactory(
            "application/json".toMediaType()
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.fda.gov/")
            .addConverterFactory(converter)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFdaApi(retrofit: Retrofit): OpenFdaApi {
        return retrofit.create()
    }

}