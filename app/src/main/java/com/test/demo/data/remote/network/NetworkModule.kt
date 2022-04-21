package com.test.demo.data.remote.network

import com.google.gson.Gson
import com.test.demo.BuildConfig
import com.test.demo.data.local.PrefsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideHttpClient(prefsHelper: PrefsHelper): OkHttpClient {
        val level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        val builder = OkHttpClient
            .Builder()
            .connectTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(prefsHelper))
            .addInterceptor(HttpLoggingInterceptor().setLevel(level))

        return builder.build()
    }
}