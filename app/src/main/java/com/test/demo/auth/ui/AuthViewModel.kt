package com.test.demo.auth.ui

import androidx.core.util.PatternsCompat
import androidx.lifecycle.asLiveData
import com.test.demo.R
import com.test.demo.auth.domain.LoginUseCase
import com.test.demo.auth.domain.RegisterUseCase
import com.test.demo.base.BaseViewModel
import com.test.demo.utils.dispatcher.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val navigationDispatcher: NavigationDispatcher
) : BaseViewModel() {

    private val email = MutableStateFlow("test.task@klikdokter.com")
    private val password = MutableStateFlow("T3stKl1kd0kt3r")

    val isOk = combine(email, password, ::canLogin)
        .asLiveData()

    val emailLiveData = email.asLiveData()
    val passwordLiveData = password.asLiveData()

    fun setEmail(newEmail: String?) {
        email.value = newEmail.orEmpty()
    }

    fun setPassword(newPassword: String?) {
        password.value = newPassword.orEmpty()
    }


    private fun canLogin(email: String, password: String): Boolean {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        if (password.length < 5) {
            return false
        }

        return true
    }

    fun login() {
        val args = LoginUseCase.LoginArgs(email.value, password.value)
        loginUseCase.execute(args)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                navigationDispatcher.dispatch { it.navigate(R.id.action_loginFragment_to_productListFragment) }
            }, ::handleError)
            .addToCompositeDisposable()
    }

    fun register() {
        val args = RegisterUseCase.RegisterArgs(email.value, password.value)
        registerUseCase.execute(args)
            .observeOn(AndroidSchedulers.mainThread())
            .bindLoading()
            .subscribe({
                event.setValue(AuthEvent.RegisterSuccessEvent)
            }, ::handleError)
            .addToCompositeDisposable()
    }
}