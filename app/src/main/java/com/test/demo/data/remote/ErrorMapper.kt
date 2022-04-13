package com.test.demo.data.remote

import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {
    fun mapError(error: HttpException): ApiError {
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
}