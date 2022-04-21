package com.test.demo.product.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.demo.data.api.model.Product
import com.test.demo.base.BaseViewModel
import com.test.demo.product.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val searchProductUseCase: SearchProductUseCase
) : BaseViewModel() {

    private val listProduct = MutableStateFlow(emptyList<Product>())
    val listProductLiveData = listProduct.asLiveData()

    private val productState = MutableStateFlow(Product.empty())
    val productStateLiveData = productState.asLiveData()

    private val searchQuery = MutableStateFlow<String?>(null)

    private var searchDisposable: Disposable? = null

    init {
        getProductList()

        searchQuery.debounce(500)
            .onEach { searchBySku(it) }
            .launchIn(viewModelScope)
    }

    fun setState(product: Product) {
        productState.value = product
    }

    fun search(query: String?) {
        searchQuery.value = query
    }

    @VisibleForTesting
    fun searchBySku(sku: String?) {
        if (sku == null) {
            return
        }

        if (sku.isEmpty()) {
            getProductList()
            return
        }

        searchDisposable?.dispose()
        searchDisposable = searchProductUseCase.execute(sku)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({ product ->
                listProduct.value = listOf(product)
            }, {
                listProduct.value = emptyList()
            })
    }

    fun getProductList(forceRefresh: Boolean = false) {
        getProductListUseCase.execute(forceRefresh)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                listProduct.value = it
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun deleteProduct(sku: String) {
        deleteProductUseCase.execute(sku)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({ product ->
                val deletedItem = listProduct.value.find { it.id == product.id } ?: return@subscribe
                val newList = listProduct.value.toMutableList()
                newList.remove(deletedItem)
                listProduct.value = newList
            }, ::handleError)
            .addToCompositeDisposable()
    }


    // ---Add, edit product
    fun addProduct() {
        if (!isValidProduct()) {
            handleError(IllegalArgumentException("Invalid product details, please check again!"))
        }

        val currentProduct = productState.value
        addProductUseCase.execute(currentProduct)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                event.setValue(ProductEvent.AddSuccess)
                getProductList()
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun editProduct() {
        if (!isValidProduct()) {
            handleError(IllegalArgumentException("Invalid product details, please check again!"))
            return
        }

        val currentProduct = productState.value
        updateProductUseCase.execute(currentProduct)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                event.setValue(ProductEvent.EditSuccess)
                getProductList()
            }, ::handleError)
            .addToCompositeDisposable()
    }

    @VisibleForTesting
    fun isValidProduct(): Boolean {
        if (productState.value.price < 0 || productState.value.qty < 0) {
            return false
        }

        return true
    }

    fun clearError() {
        errorEvent.dropEvent()
    }
}