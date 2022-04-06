package com.test.demo.features.product.list

import com.test.demo.data.remote.Api
import com.test.demo.data.remote.model.Product
import com.test.demo.features.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ProductListViewModel(private val api: Api): BaseViewModel() {
    val listProduct = MutableStateFlow(emptyList<Product>())

    init {
        getProductList()
    }

    fun getProductList() {
        launchLoading {
            listProduct.value = api.getListProduct()
        }
    }

    fun deleteProduct(sku: String) {
        launchJob {
            val deletedProduct = api.deleteProduct(sku)
            val index = listProduct.value.indexOfFirst { it.id == deletedProduct.id }
            val newList = listProduct.value.toMutableList()
            newList.removeAt(index)

            listProduct.value = newList
        }
    }
}