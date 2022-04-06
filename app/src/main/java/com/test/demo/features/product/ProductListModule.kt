package com.test.demo.features.product

import com.test.demo.features.product.add.AddProductViewModel
import com.test.demo.features.product.list.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productListModule
    get() = module {
        viewModel { ProductListViewModel(get()) }
        viewModel { AddProductViewModel(get()) }
    }