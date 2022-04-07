package com.test.demo.data.remote.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?,
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
    val updatedAt: String,
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("message")
    val message: String?
): Parcelable