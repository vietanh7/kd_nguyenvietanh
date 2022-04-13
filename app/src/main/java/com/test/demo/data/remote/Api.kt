package com.test.demo.data.remote

import com.test.demo.data.remote.Api.Companion.TOKEN_EXPIRED_MESSAGE
import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import com.test.demo.utils.dispatcher.TokenExpiredDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.concurrent.CancellationException
import javax.inject.Inject

interface Api {
    suspend fun login(email: String, password: String): Token

    suspend fun register(email: String, password: String): RegisterResponse

    suspend fun getListProduct(): List<Product>

    suspend fun deleteProduct(sku: String): Product

    suspend fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Product

    suspend fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Product

    suspend fun searchProduct(sku: String): Product

    companion object {
        const val TOKEN_EXPIRED_MESSAGE = "Provided token is expired."
    }
}

class ApiIml @Inject constructor(
    private val apiService: ApiService,
    private val errorMapper: ErrorMapper,
    private val tokenExpiredDispatcher: TokenExpiredDispatcher
) : Api {

    private suspend inline fun <T> wrapErrorCall(crossinline block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            try {
                block.invoke()
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                val apiError = errorMapper.mapError(e)
                if (apiError.message == TOKEN_EXPIRED_MESSAGE) {
                    tokenExpiredDispatcher.dispatch()
                }

                throw apiError
            }
        }
    }

    override suspend fun login(email: String, password: String): Token {
        return wrapErrorCall { apiService.login(email, password) }
    }

    override suspend fun register(email: String, password: String): RegisterResponse {
        return apiService.register(email, password)
    }

    override suspend fun getListProduct(): List<Product> {
        return wrapErrorCall { apiService.getProductList() }
    }

    override suspend fun deleteProduct(sku: String): Product {
        return wrapErrorCall { apiService.deleteProduct(sku) }
    }

    override suspend fun addProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Product {
        return wrapErrorCall { apiService.addProduct(sku, productName, quantity, price, unit, status) }
    }

    override suspend fun updateProduct(
        sku: String,
        productName: String,
        quantity: Int,
        price: Int,
        unit: String,
        status: Int
    ): Product {
        return wrapErrorCall { apiService.updateProduct(sku, productName, quantity, price, unit, status) }
    }

    override suspend fun searchProduct(sku: String): Product {
        return wrapErrorCall {
            val product = apiService.searchProduct(sku)
            if (product.success == false) {
                throw ApiError(product.message.orEmpty(), -1)
            }

            product
        }
    }
}