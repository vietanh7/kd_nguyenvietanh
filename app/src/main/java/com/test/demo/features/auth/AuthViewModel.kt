package com.test.demo.features.auth

import android.util.Patterns
import androidx.lifecycle.asLiveData
import com.test.demo.data.local.PrefsHelper
import com.test.demo.data.remote.Api
import com.test.demo.data.remote.ApiError
import com.test.demo.features.base.BaseViewModel
import com.test.demo.features.base.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class AuthViewModel(
    private val api: Api,
    private val prefsHelper: PrefsHelper
) : BaseViewModel() {

    val email = MutableStateFlow("test.task@klikdokter.com")
    val password = MutableStateFlow("T3stKl1kd0kt3r")
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
            event.postValue(AuthEvent.LoginSuccessEvent)
        }
    }

    fun register() {
        launchLoading {
            val user = api.register(email.value, password.value)
            if (!user.success) {
                throw ApiError("Failed to register", -1)
            }

            event.setValue(AuthEvent.RegisterSuccessEvent)
        }
    }
}