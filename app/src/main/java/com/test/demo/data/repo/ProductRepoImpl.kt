package com.test.demo.data.repo

import com.test.demo.data.ThrottleHelper
import com.test.demo.data.api.common.ApiConstants
import com.test.demo.data.api.model.Product
import com.test.demo.data.api.product.ProductApi
import com.test.demo.data.db.product.ProductDao
import com.test.demo.data.db.product.ProductEntity
import com.test.demo.product.domain.ProductRepo
import com.test.demo.utils.mapList
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepoImpl @Inject constructor(
    private val productDao: ProductDao,
    private val api: ProductApi,
    private val throttleHelper: ThrottleHelper
): ProductRepo {

    override fun getListProduct(force: Boolean): Single<List<Product>> {
        val shouldRefresh = throttleHelper.canRefresh(ApiConstants.GET_PRODUCTS_ENDPOINT, PRODUCT_REFRESH_TIMEOUT)
        val resultSingle = if (force || shouldRefresh) {
            api.getListProduct()
                .flatMap { products ->
                    val entities = products.map { ProductEntity.from(it) }
                    productDao.clearProduct()
                        .andThen(productDao.saveProducts(entities))
                        .toSingle { products }
                }.doOnSuccess { throttleHelper.updateRefreshTime(ApiConstants.GET_PRODUCTS_ENDPOINT) }
        } else {
            productDao
                .getAllProduct()
                .mapList { it.toProduct() }
        }

        return resultSingle.subscribeOn(Schedulers.io())
    }

    override fun observeProducts(): Flowable<List<Product>> {
        return productDao.observeProducts().mapList { it.toProduct() }
    }

    override fun updateProduct(product: Product): Single<Product> {
        return api.updateProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        )
            .flatMap { productDao.updateProduct(ProductEntity.from(it)).toSingle { it } }
            .subscribeOn(Schedulers.io())
    }

    override fun addProduct(product: Product): Single<Product> {
        return api.addProduct(
            product.sku, product.productName,
            product.qty, product.price,
            product.unit, product.status
        )
            .flatMap { productDao.saveProduct(ProductEntity.from(it)).toSingle { it } }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProduct(sku: String): Single<Product> {
        return api.deleteProduct(sku)
            .flatMap { product ->
                productDao.deleteItem(product.id).toSingle { product }
            }.subscribeOn(Schedulers.io())
    }

    override fun searchProduct(sku: String): Single<Product> = api.searchProduct(sku)

    companion object {
        const val PRODUCT_REFRESH_TIMEOUT = 60 * 1000L
    }
}