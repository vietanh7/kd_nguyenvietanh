package com.test.demo.data.remote.api

import com.test.demo.utils.dispatcher.TokenExpiredDispatcher
import io.reactivex.rxjava3.core.Single
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ErrorHandler @Inject constructor(private val tokenExpiredDispatcher: TokenExpiredDispatcher) {
    fun mapError(error: HttpException): ApiError {
        val errorBody = error.response()?.errorBody()?.string()
        var message = error.response()?.message().orEmpty()
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
            ApiError(message, error.code())
        }
    }

    fun <T : Any> handleError(throwable: Throwable): Single<T> {
        return when (throwable) {
            is HttpException -> {
                val apiError = mapError(throwable)
                if (apiError.message == ApiConstants.TOKEN_EXPIRED_MESSAGE) {
                    tokenExpiredDispatcher.dispatch()
                }

                Single.error(apiError)
            }
            else -> Single.error(throwable)
        }
    }
}