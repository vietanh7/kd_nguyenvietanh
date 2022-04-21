package com.test.demo.data.api.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("data")
    val data: Data,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
) {

    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("update_at")
        val updatedAt: String
    )
}