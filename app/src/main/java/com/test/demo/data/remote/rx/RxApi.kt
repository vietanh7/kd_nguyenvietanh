package com.test.demo.data.remote.rx

import com.test.demo.data.remote.ApiError
import com.test.demo.data.remote.ErrorMapper
import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import com.test.demo.utils.dispatcher.TokenExpiredDispatcher
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

interface RxApi {
    fun login(email: String, password: String): Single<Token>

    fun register(email: String, password: String): Single<RegisterResponse>

    fun getListProduct(): Single<List<Product>>

    fun deleteProduct(sku: String): Single<Product>

    fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Single<Product>

    fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Single<Product>

    fun searchProduct(sku: String): Single<Product>

    companion object {
        const val TOKEN_EXPIRED_MESSAGE = "Provided token is expired."
    }
}

class RxApiIml @Inject constructor(
    private val service: RxApiService,
    private val errorMapper: ErrorMapper,
    private val tokenExpiredDispatcher: TokenExpiredDispatcher
) : RxApi {

    private fun <T : Any> mapError(err: Throwable): Single<T> {
        return when (err) {
            is HttpException -> {
                val apiError = errorMapper.mapError(err)
                if (apiError.message == RxApi.TOKEN_EXPIRED_MESSAGE) {
                    tokenExpiredDispatcher.dispatch()
                }

                Single.error(apiError)
            }
            else -> Single.error(err)
        }
    }

    private fun <T : Any> Single<T>.wrapError(): Single<T> {
        return this.onErrorResumeNext(::mapError)
            .subscribeOn(Schedulers.io())
    }

    override fun login(email: String, password: String): Single<Token> {
        return service.login(email, password)
            .wrapError()
    }

    override fun register(email: String, password: String): Single<RegisterResponse> {
        return service.register(email, password).subscribeOn(Schedulers.io())
    }

    override fun getListProduct(): Single<List<Product>> {
        return service.getProductList().onErrorResumeNext(::mapError)
            .wrapError()
    }

    override fun deleteProduct(sku: String): Single<Product> {
        return service.deleteProduct(sku).onErrorResumeNext(::mapError)
            .wrapError()
    }

    override fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Single<Product> {
        return service.addProduct(sku, productName, quantity, price, unit, status)
            .wrapError()
    }

    override fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Single<Product> {
        return service.updateProduct(sku, productName, quantity, price, unit, status)
            .wrapError()
    }

    override fun searchProduct(sku: String): Single<Product> {
        return service.searchProduct(sku)
            .wrapError()
            .map { product ->
                if (product.success == false) {
                    throw ApiError(product.message.orEmpty(), 400)
                }

                product
            }
    }

}