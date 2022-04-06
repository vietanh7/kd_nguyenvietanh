package com.test.demo.features.product

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productListModule
    get() = module {
        viewModel { ProductListViewModel(get()) }
    }