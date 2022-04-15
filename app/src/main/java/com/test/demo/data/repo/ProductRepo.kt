package com.test.demo.data.repo

import com.test.demo.data.ThrottleHelper
import com.test.demo.data.db.AppDb
import com.test.demo.data.db.product.ProductEntity
import com.test.demo.data.remote.api.ApiConstants
import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.product.ProductApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@ViewModelScoped
class ProductRepo @Inject constructor(
    private val db: AppDb,
    private val api: ProductApi,
    private val throttleHelper: ThrottleHelper
) {
    companion object {
        const val PRODUCT_REFRESH_TIMEOUT = 60 * 1000L
    }

    private val productDao = db.productDao()

    fun getListProduct(force: Boolean = false): Single<List<Product>> {
        val shouldRefresh = throttleHelper.canRefresh(ApiConstants.GET_PRODUCTS_ENDPOINT, PRODUCT_REFRESH_TIMEOUT)
        val resultSingle = if (force || shouldRefresh) {
            api.getListProduct().flatMap { products ->
                val entities = products.map { ProductEntity.from(it) }
                productDao.clearProduct()
                    .andThen(productDao.saveProducts(entities))
                    .toSingle { products }
            }
        } else {
            productDao
                .getAllProduct().map { entities -> entities.map { it.toProduct() } }
        }

        return resultSingle.subscribeOn(Schedulers.io())
    }

    fun updateProduct(product: Product): Single<Product> {
        return api.updateProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        )
            .flatMap { productDao.updateProduct(ProductEntity.from(it)).toSingle { it } }
            .subscribeOn(Schedulers.io())
    }

    fun addProduct(product: Product): Single<Product> {
        return api.addProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        )
            .flatMap { productDao.saveProduct(ProductEntity.from(it)).toSingle { it } }
            .subscribeOn(Schedulers.io())
    }

    fun deleteProduct(sku: String): Single<Product> {
        return api.deleteProduct(sku)
            .flatMap { product ->
                productDao.deleteItem(product.id).toSingle { product }
            }.subscribeOn(Schedulers.io())
    }

    fun searchProduct(sku: String): Single<Product> {
        return api.searchProduct(sku)
    }
}