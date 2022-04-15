package com.test.demo.data.remote.network

import com.test.demo.data.local.PrefsHelper
import com.test.demo.utils.asBearerToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val prefsHelper: PrefsHelper): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefsHelper.getToken()
        val request = chain.request()
            .newBuilder()
            .addHeader(NetworkConstants.AUTHORIZATION_HEADER, token.asBearerToken())
            .build()

        return chain.proceed(request)
    }
}