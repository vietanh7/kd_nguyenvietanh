package com.test.demo.features.login

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule
    get() = module {
        viewModel { LoginViewModel(get(), get()) }
    }