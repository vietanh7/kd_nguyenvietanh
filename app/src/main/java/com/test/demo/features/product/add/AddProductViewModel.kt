package com.test.demo.features.product.add

import com.test.demo.data.remote.Api
import com.test.demo.features.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AddProductViewModel(private val api: Api): BaseViewModel() {
    val sku = MutableStateFlow("")
    val productName = MutableStateFlow("")
    val quantity = MutableStateFlow(0)
    val price = MutableStateFlow(0)
    val unit = MutableStateFlow("")
    val state = MutableStateFlow(0)


    init {

    }


}