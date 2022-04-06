package com.test.demo.data.remote.model


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Any,
    @SerializedName("price")
    val price: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("qty")
    val qty: Int,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("updated_at")
    val updatedAt: String
)