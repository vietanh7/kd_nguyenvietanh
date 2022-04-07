package com.test.demo.features.product

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productModule
    get() = module {
        viewModel { ProductViewModel(get()) }
    }