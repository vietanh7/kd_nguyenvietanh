package com.test.demo.data

import com.test.demo.data.remote.Api
import com.test.demo.data.remote.ApiIml
import com.test.demo.data.remote.ApiService
import org.koin.dsl.module

val dataModule
    get() = module {
        single { provideApi(get()) }
    }

private fun provideApi(service: ApiService): Api {
    return ApiIml(service)
}