package com.test.demo.data.remote.auth

import com.test.demo.data.remote.api.ErrorHandler
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface AuthApi {
    fun login(email: String, password: String): Single<Token>

    fun register(email: String, password: String): Single<RegisterResponse>
}


class AuthApiImpl @Inject constructor(
    private val authService: AuthService,
    private val errorHandler: ErrorHandler
) : AuthApi {

    private fun <T : Any> Single<T>.wrapError(): Single<T> {
        return this.onErrorResumeNext(errorHandler::handleError)
            .subscribeOn(Schedulers.io())
    }

    override fun login(email: String, password: String): Single<Token> {
        return authService.login(email, password)
            .wrapError()
    }

    override fun register(email: String, password: String): Single<RegisterResponse> {
        return authService.register(email, password).subscribeOn(Schedulers.io())
    }
}