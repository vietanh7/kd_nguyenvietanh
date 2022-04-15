package com.test.demo.data.remote.product

import com.test.demo.data.remote.api.ErrorHandler
import com.test.demo.data.remote.model.Product
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface ProductApi {
    fun getListProduct(): Single<List<Product>>

    fun deleteProduct(sku: String): Single<Product>

    fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Long,
        unit: String,
        status: Int
    ): Single<Product>

    fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Long,
        unit: String,
        status: Int
    ): Single<Product>

    fun searchProduct(sku: String): Single<Product>
}

class ProductApiImpl @Inject constructor(
    private val service: ProductApiService,
    private val errorHandler: ErrorHandler
): ProductApi {

    private fun <T : Any> Single<T>.wrapError(): Single<T> {
        return this.onErrorResumeNext(errorHandler::handleError)
            .subscribeOn(Schedulers.io())
    }

    override fun getListProduct(): Single<List<Product>> {
        return service.getProductList()
            .wrapError()
    }

    override fun deleteProduct(sku: String): Single<Product> {
        return service.deleteProduct(sku)
            .wrapError()
            .map { it.validate() }
    }

    override fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Long,
        unit: String,
        status: Int
    ): Single<Product> {
        return service.addProduct(sku, productName, quantity, price, unit, status)
            .wrapError()
            .map { it.validate() }
    }

    override fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Long,
        unit: String,
        status: Int
    ): Single<Product> {
        return service.updateProduct(sku, productName, quantity, price, unit, status)
            .wrapError()
            .map { it.validate() }
    }

    override fun searchProduct(sku: String): Single<Product> {
        return service.searchProduct(sku)
            .wrapError()
            .map { it.validate() }
    }
}