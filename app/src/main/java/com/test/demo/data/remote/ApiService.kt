package com.test.demo.data.remote

import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String): Token

    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("api/items")
    suspend fun getProductList(): List<Product>

    @FormUrlEncoded
    @POST("api/item/delete")
    suspend fun deleteProduct(@Field("sku") sku: String): Product

    @FormUrlEncoded
    @POST("api/item/add")
    suspend fun addProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Product

    @FormUrlEncoded
    @POST("api/item/update")
    suspend fun updateProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Product

    @FormUrlEncoded
    @POST("api/item/search")
    suspend fun searchProduct(@Field("sku") sku: String): Product
}