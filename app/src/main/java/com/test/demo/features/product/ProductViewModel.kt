package com.test.demo.features.product

import androidx.lifecycle.viewModelScope
import com.test.demo.data.remote.Api
import com.test.demo.data.remote.model.Product
import com.test.demo.features.base.BaseViewModel
import com.test.demo.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProductViewModel(private val api: Api): BaseViewModel() {
    val listProduct = MutableStateFlow(emptyList<Product>())
    val needReload = SingleLiveEvent(true)

    val sku = MutableStateFlow("")
    val productName = MutableStateFlow("")
    val quantity = MutableStateFlow(0)
    val price = MutableStateFlow(0)
    val unit = MutableStateFlow("")
    val status = MutableStateFlow(0)

    val searchQuery = MutableStateFlow("")

    init {
        searchQuery.debounce(300)
            .onEach { searchBySku(it) }
            .launchIn(viewModelScope)
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

    fun searchBySku(sku: String?) {
        if (sku.isNullOrEmpty()) {
            getProductList()
            return
        }

        launchLoading {
            try {
                val product = api.searchProduct(sku)
                listProduct.value = listOf(product)
            } catch (e: Exception) {
                listProduct.value = emptyList()
            }
        }
    }


    // ---Add, edit product
    fun addProduct() {
        launchLoading {
            validateProduct()
            api.addProduct(
                sku.value,
                productName.value,
                quantity.value,
                price.value,
                unit.value,
                status.value
            )

            event.setValue(ProductEvent.AddSuccess)
            needReload.setValue(true)
        }
    }

    fun editProduct() {
        launchLoading {
            validateProduct()
            api.updateProduct(
                sku.value,
                productName.value,
                quantity.value,
                price.value,
                unit.value,
                status.value
            )

            event.setValue(ProductEvent.EditSuccess)
            needReload.setValue(true)
        }
    }

    fun clearEditData() {
        initialized = false
        sku.value = ""
        productName.value = ""
        quantity.value = 0
        price.value = 0
        unit.value = ""
        status.value = 0
    }

    private fun validateProduct() {
        if (price.value < 0 || quantity.value < 0) {
            throw IllegalArgumentException("Invalid product details, please check again!")
        }
    }

    private var initialized = false
    fun init(product: Product) {
        if (!initialized) {
            initialized = true
            sku.value = product.sku
            productName.value = product.productName
            quantity.value = product.qty
            price.value = product.price
            unit.value = product.unit
            status.value = product.status
        }
    }
}