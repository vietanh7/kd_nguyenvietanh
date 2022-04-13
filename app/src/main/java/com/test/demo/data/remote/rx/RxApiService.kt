package com.test.demo.data.remote.rx

import com.test.demo.data.remote.model.Product
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RxApiService {

    @FormUrlEncoded
    @POST("api/auth/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<RegisterResponse>

    @GET("api/items")
    fun getProductList(): Single<List<Product>>

    @FormUrlEncoded
    @POST("api/item/delete")
    fun deleteProduct(@Field("sku") sku: String): Single<Product>

    @FormUrlEncoded
    @POST("api/item/add")
    fun addProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Single<Product>

    @FormUrlEncoded
    @POST("api/item/update")
    fun updateProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Int,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Single<Product>

    @FormUrlEncoded
    @POST("api/item/search")
    fun searchProduct(@Field("sku") sku: String): Single<Product>
}