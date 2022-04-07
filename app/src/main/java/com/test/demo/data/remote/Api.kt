package com.test.demo.data.remote

import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception
import java.util.concurrent.CancellationException

interface Api {
    suspend fun login(email: String, password: String): Token

    suspend fun register(email: String, password: String): RegisterResponse

    suspend fun getListProduct(): List<Product>

    suspend fun deleteProduct(sku: String): Product

    suspend fun addProduct(sku: String, productName: String, quantity: Int, price: Int, unit: String, status: Int): Product

    suspend fun updateProduct(sku: String, productName: String, quantity: Int, price: Int, unit: String, status: Int): Product
}

class ApiIml(private val apiService: ApiService): Api {

    private inline fun <T> wrapErrorCall(block: () -> T): T {
        try {
            return block.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            throw parseHttpError(e)
        }
    }

    private fun parseHttpError(error: HttpException): ApiError {
        val errorBody = error.response()?.errorBody()?.string()
        var message = error.message()
        var code = error.code()
        return try {
            val json = JSONObject(errorBody!!)
            if (json.has("error")) {
                message = json.getString("error")
            }

            if (json.has("message")) {
                message = json.getString("message")
            }

            if (json.has("code")) {
                code = json.getInt("code")
            }

            return ApiError(message, code)
        } catch (e: Exception) {
            ApiError(error.message(), error.code())
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
}