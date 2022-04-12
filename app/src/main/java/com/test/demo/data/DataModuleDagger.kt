package com.test.demo.data

import android.content.Context
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.ApiIml
import com.test.demo.data.remote.ApiService
import com.test.demo.utils.dispatcher.TokenExpiredDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModuleDagger {

    @Provides
    fun providePrefsHelper(context: Context): PrefsHelper {
        return PrefsHelper(context)
    }

    @Provides
    fun provideApi(service: ApiService, dispatcher: TokenExpiredDispatcher): ApiIml {
        return ApiIml(service, dispatcher)
    }
}