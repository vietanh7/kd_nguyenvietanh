package com.test.demo.data.remote

import com.google.gson.Gson
import com.test.demo.BuildConfig
import com.test.demo.data.local.PrefsHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT = 20L

val networkModule
    get() = module {
        single { provideHttpClient(BuildConfig.DEBUG, get()) }
        single { provideGson() }
        factory { provideRetrofitBuilder(get(), get()) }

        single { provideApiService(get()) }
    }

private fun provideApiService(builder: Retrofit.Builder): ApiService {
    return builder.baseUrl(BuildConfig.BASE_URL)
        .build()
        .create(ApiService::class.java)
}

private fun provideRetrofitBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
}

private fun provideGson(): Gson {
    return Gson()
}

private fun provideHttpClient(enableLogging: Boolean, prefsHelper: PrefsHelper): OkHttpClient {
    val level = if (enableLogging)
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