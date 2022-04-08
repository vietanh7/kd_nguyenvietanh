package com.test.demo.features.auth

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule
    get() = module {
        viewModel { AuthViewModel(get(), get(), get()) }
    }