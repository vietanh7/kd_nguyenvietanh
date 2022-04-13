package com.test.demo.data.remote.network

import com.google.gson.Gson
import com.test.demo.BuildConfig
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleDagger {

    private const val NETWORK_TIMEOUT = 20L

    @Singleton
    @Provides
    fun provideApiService(builder: Retrofit.Builder): ApiService {
        return builder.baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideHttpClient(prefsHelper: PrefsHelper): OkHttpClient {
        val level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        val builder = OkHttpClient
            .Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(prefsHelper))
            .addInterceptor(HttpLoggingInterceptor().setLevel(level))

        return builder.build()
    }
}