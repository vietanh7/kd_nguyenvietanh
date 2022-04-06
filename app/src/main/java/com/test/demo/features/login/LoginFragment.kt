package com.test.demo.features.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.asLiveData
import com.test.demo.R
import com.test.demo.databinding.LoginFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.base.Event
import com.test.demo.features.main.MainActivity
import com.test.demo.features.product.list.ProductListFragment
import com.test.demo.utils.setTextIfChanged
import com.test.demo.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: BaseFragment<LoginFragmentBinding, LoginViewModel>(R.layout.login_fragment) {
    override val binding by viewBinding { LoginFragmentBinding.bind(it) }
    override val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVm()
    }

    private fun observeVm() {
        viewModel.loading.observe(::handleLoading)
        viewModel.email.asLiveData().observe { binding.emailEdt.setTextIfChanged(it)}
        viewModel.password.asLiveData().observe { binding.passwordEdt.setTextIfChanged(it) }
        viewModel.isOk.observe { binding.btnLogin.isEnabled = it }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.loadingIndicator.isVisible = isLoading
    }

    override fun onNewEvent(event: Event) {
        when(event) {
            is LoginEvent.LoginSuccessEvent -> {
                val host = activity as? MainActivity ?: return
                host.changeFragment(ProductListFragment.newInstance(), false)
            }
        }

        super.onNewEvent(event)
    }

    private fun setup() {
        with(binding) {
            btnLogin.setOnClickListener { viewModel.login() }
            emailEdt.doAfterTextChanged { viewModel.email.value = it?.toString().orEmpty() }
            passwordEdt.doAfterTextChanged { viewModel.password.value = it?.toString().orEmpty() }
        }
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}