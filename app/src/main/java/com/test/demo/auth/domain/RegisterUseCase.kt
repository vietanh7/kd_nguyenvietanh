package com.test.demo.auth.domain

import com.test.demo.base.CompletableUseCase
import com.test.demo.data.api.auth.AuthApi
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

interface RegisterUseCase: CompletableUseCase<RegisterUseCase.RegisterArgs> {
    data class RegisterArgs(
        val email: String,
        val password: String
    )
}

class RegisterUseCaseImpl @Inject constructor(private val authApi: AuthApi): RegisterUseCase {
    override fun execute(args: RegisterUseCase.RegisterArgs): Completable = authApi
        .register(args.email, args.password)
        .flatMapCompletable { Completable.complete() }

}