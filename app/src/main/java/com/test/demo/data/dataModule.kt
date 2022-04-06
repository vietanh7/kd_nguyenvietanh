package com.test.demo.data

import android.content.Context
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.Api
import com.test.demo.data.remote.ApiIml
import com.test.demo.data.remote.ApiService
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule
 get() = module {
     single { providePrefsHelper(get()) }
     single { provideApi(get()) } bind Api::class
    }

private fun providePrefsHelper(context: Context): PrefsHelper {
    return PrefsHelper(context)
}

private fun provideApi(service: ApiService): ApiIml {
    return ApiIml(service)
}