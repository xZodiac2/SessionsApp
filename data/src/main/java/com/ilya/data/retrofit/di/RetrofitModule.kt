package com.ilya.data.retrofit.di

import com.ilya.data.retrofit.SessionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {
    @Provides
    internal fun provideApi(): SessionsApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
        
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(SessionsApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        
        return retrofit.create(SessionsApi::class.java)
    }
}