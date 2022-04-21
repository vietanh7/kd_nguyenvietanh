package com.test.demo.auth.domain

import com.test.demo.base.SingleUseCase
import com.test.demo.data.api.auth.AuthApi
import com.test.demo.data.prefs.PrefsHelper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface LoginUseCase: SingleUseCase<LoginUseCase.LoginArgs, String> {

    data class LoginArgs(
        val email: String,
        val password: String
    )
}

class LoginUseCaseImpl @Inject constructor(
    private val authApi: AuthApi,
    private val prefsHelper: PrefsHelper
) : LoginUseCase {
    override fun execute(args: LoginUseCase.LoginArgs): Single<String> = authApi
        .login(args.email, args.password)
        .map { tokenRes ->
            if (tokenRes.token.isEmpty()) {
                throw IllegalArgumentException("Empty token")
            }

            prefsHelper.saveToken(tokenRes.token)
            tokenRes.token
        }

}