package com.test.demo.features.product

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.demo.data.remote.Api
import com.test.demo.data.remote.model.Product
import com.test.demo.features.base.BaseViewModel
import com.test.demo.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val api: Api): BaseViewModel() {
    val listProduct = MutableStateFlow(emptyList<Product>())
    val needReload = SingleLiveEvent(true)

    private val productState = MutableStateFlow(Product.empty())
    val productStateLiveData = productState.asLiveData()

    fun setSate(product: Product) {
        productState.value = product
    }

    private val searchQuery = MutableStateFlow<String?>("")

    init {
        searchQuery.debounce(500)
            .onEach { searchBySku(it) }
            .launchIn(viewModelScope)
    }

    fun search(query: String?) {
        searchQuery.value = query
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

    private var searchJob: Job? = null
    fun searchBySku(sku: String?) {
        if (sku.isNullOrEmpty()) {
            getProductList()
            return
        }

        searchJob?.cancel()
        searchJob = launchLoading {
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
            val currentProduct = productState.value
            api.addProduct(
                currentProduct.sku,
                currentProduct.productName,
                currentProduct.qty,
                currentProduct.price,
                currentProduct.unit,
                currentProduct.status
            )

            event.setValue(ProductEvent.AddSuccess)
            needReload.setValue(true)
        }
    }

    fun editProduct() {
        launchLoading {
            validateProduct()
            val currentProduct = productState.value
            api.updateProduct(
                currentProduct.sku,
                currentProduct.productName,
                currentProduct.qty,
                currentProduct.price,
                currentProduct.unit,
                currentProduct.status
            )

            event.setValue(ProductEvent.EditSuccess)
            needReload.setValue(true)
        }
    }

    fun clearEditData() {
        initialized = false
        productState.value = Product.empty()
    }

    private fun validateProduct() {
        if (productState.value.price < 0 || productState.value.qty < 0) {
            throw IllegalArgumentException("Invalid product details, please check again!")
        }
    }

    private var initialized = false
    fun init(product: Product) {
        if (!initialized) {
            initialized = true
            productState.value = Product.empty().copy(
                productName = product.productName,
                sku = product.sku,
                qty = product.qty,
                price = product.price,
                unit = product.unit,
                status = product.status
            )
        }
    }
}