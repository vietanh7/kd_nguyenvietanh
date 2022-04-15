package com.test.demo.features.auth

import android.util.Patterns
import androidx.lifecycle.asLiveData
import com.test.demo.R
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.Api
import com.test.demo.data.remote.ApiError
import com.test.demo.features.base.BaseViewModel
import com.test.demo.utils.dispatcher.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: Api,
    private val prefsHelper: PrefsHelper,
    private val navigationDispatcher: NavigationDispatcher
) : BaseViewModel() {

    val email = MutableStateFlow("test.task@klikdokter.com")
    val password = MutableStateFlow("T3stKl1kd0kt3r")

    val isOk = combine(email, password, ::canLogin)
        .asLiveData(createExceptionHandler())


    private fun canLogin(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        if (password.length < 5) {
            return false
        }

        return true
    }

    fun login() {
        api.login(email.value, password.value)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({ token ->
                if (token.token.isEmpty()) {
                    errorEvent.setValue(IllegalArgumentException("Token is empty"))
                    return@subscribe
                }

                prefsHelper.saveToken(token.token)
                navigationDispatcher.dispatch { it.navigate(R.id.action_loginFragment_to_productListFragment) }
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun register() {
        api.register(email.value, password.value)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                if (!it.success) {
                    errorEvent.setValue(ApiError("Failed to register", -1))
                    return@subscribe
                }

                event.setValue(AuthEvent.RegisterSuccessEvent)
            }, ::handleError)
            .addToCompositeDisposable()
    }
}