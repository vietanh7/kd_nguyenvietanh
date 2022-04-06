package com.test.demo.data.remote.model

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
        val email: String,
        val id: Int,
        val updatedAt: String
    )
}