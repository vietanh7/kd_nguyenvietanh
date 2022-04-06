package com.test.demo.features.login

import android.util.Patterns
import androidx.lifecycle.asLiveData
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.Api
import com.test.demo.features.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel(
    private val api: Api,
    private val prefsHelper: PrefsHelper
) : BaseViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val isOk = combine(email, password, this::canLogin).asLiveData()

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
        if (isLoading.value) {
            return
        }

        launchLoading {
            val token = api.login(email.value, password.value)
            if (token.token.isEmpty()) {
                throw IllegalArgumentException("Token is empty")
            }

            prefsHelper.saveToken(token.token)
            event.postValue(LoginEvent.LoginSuccessEvent)
        }
    }
}