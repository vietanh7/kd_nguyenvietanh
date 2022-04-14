package com.test.demo.features.product

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.rx.RxApi
import com.test.demo.data.remote.rx.RxApiService
import com.test.demo.features.base.rx.BaseRxViewModel
import com.test.demo.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RxProductViewModel @Inject constructor(private val api: RxApi) : BaseRxViewModel() {
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
        api.getListProduct()
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                listProduct.value = it
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun deleteProduct(sku: String) {
        api.deleteProduct(sku)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({ product ->
                val index = listProduct.value.indexOfFirst { it.id == product.id }
                val newList = listProduct.value.toMutableList()
                newList.removeAt(index)

                listProduct.value = newList
            }, ::handleError)
            .addToCompositeDisposable()
    }

    private var searchDisposable: Disposable? = null
    fun searchBySku(sku: String?) {
        if (sku.isNullOrEmpty()) {
            getProductList()
            return
        }

        searchDisposable?.dispose()
        searchDisposable = api.searchProduct(sku)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({ product ->
                listProduct.value = listOf(product)
            }, {
                listProduct.value = emptyList()
            })
    }


    // ---Add, edit product
    fun addProduct() {
        launchLoading {
            if (!isValidProduct()) {
                handleError(IllegalArgumentException("Invalid product details, please check again!"))
            }

            val currentProduct = productState.value
            api.addProduct(
                currentProduct.sku,
                currentProduct.productName,
                currentProduct.qty,
                currentProduct.price,
                currentProduct.unit,
                currentProduct.status
            ).observeOn(AndroidSchedulers.mainThread())
                .bindLoading()
                .subscribe({
                    event.setValue(ProductEvent.AddSuccess)
                    needReload.setValue(true)
                }, ::handleError)
                .addToCompositeDisposable()
        }
    }

    fun editProduct() {
        if (!isValidProduct()) {
            handleError(IllegalArgumentException("Invalid product details, please check again!"))
            return
        }

        val currentProduct = productState.value
        api.updateProduct(
            currentProduct.sku,
            currentProduct.productName,
            currentProduct.qty,
            currentProduct.price,
            currentProduct.unit,
            currentProduct.status
        ).observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                event.setValue(ProductEvent.EditSuccess)
                needReload.setValue(true)
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun clearEditData() {
        initialized = false
        productState.value = Product.empty()
    }

    private fun isValidProduct(): Boolean {
        if (productState.value.price < 0 || productState.value.qty < 0) {
            return false
        }

        return true
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