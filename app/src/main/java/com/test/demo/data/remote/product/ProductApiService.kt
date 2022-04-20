package com.test.demo.data.remote.product

import com.test.demo.data.remote.api.ApiConstants
import com.test.demo.data.remote.model.Product
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApiService {

    @GET(ApiConstants.GET_PRODUCTS_ENDPOINT)
    fun getProductList(): Single<List<Product>>

    @FormUrlEncoded
    @POST(ApiConstants.DELETE_PRODUCT_ENDPOINT)
    fun deleteProduct(@Field("sku") sku: String): Single<Product>

    @FormUrlEncoded
    @POST(ApiConstants.ADD_PRODUCT_ENDPOINT)
    fun addProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Long,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Single<Product>

    @FormUrlEncoded
    @POST(ApiConstants.UPDATE_PRODUCT_ENDPOINT)
    fun updateProduct(
        @Field("sku") sku: String,
        @Field("product_name") productName: String,
        @Field("qty") quantity: Int,
        @Field("price") price: Long,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Single<Product>

    @FormUrlEncoded
    @POST(ApiConstants.SEARCH_PRODUCT_ENDPOINT)
    fun searchProduct(@Field("sku") sku: String): Single<Product>
}