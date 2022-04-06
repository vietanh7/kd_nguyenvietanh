package com.test.demo.data.remote

import com.test.demo.data.local.PrefsHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(val prefsHelper: PrefsHelper): Interceptor {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    private fun String?.bearerToken(): String {
        return "Bearer ${this.orEmpty()}"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefsHelper.getToken()
        val request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION_HEADER, token.bearerToken())
            .build()

        return chain.proceed(request)
    }
}