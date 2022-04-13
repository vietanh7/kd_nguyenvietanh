package com.test.demo.features.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.test.demo.R
import com.test.demo.databinding.LoginFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.base.Event
import com.test.demo.utils.setTextIfChanged
import com.test.demo.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: BaseFragment<LoginFragmentBinding, RxAuthViewModel>(R.layout.login_fragment) {
    override val binding by viewBinding { LoginFragmentBinding.bind(it) }
    override val viewModel: RxAuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVm()
    }

    private fun setup() {
        with(binding) {
            title.setText(R.string.register)
            actionBtn.setText(R.string.register)
            actionBtn.setOnClickListener { viewModel.register() }
            emailEdt.doAfterTextChanged { viewModel.email.value = it?.toString().orEmpty() }
            passwordEdt.doAfterTextChanged { viewModel.password.value = it?.toString().orEmpty() }
            navigationText.setText(R.string.login)
            navigationText.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        }
    }

    private fun observeVm() {
        viewModel.email.asLiveData().observe { binding.emailEdt.setTextIfChanged(it)}
        viewModel.password.asLiveData().observe { binding.passwordEdt.setTextIfChanged(it) }
        viewModel.isOk.observe { binding.actionBtn.isEnabled = it }
    }

    override fun onNewEvent(event: Event) {
        when(event) {
            is AuthEvent.RegisterSuccessEvent -> {
                showMessageAndDo("Register success, you can now login") {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }

            else -> super.onNewEvent(event)
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        binding.actionBtn.isEnabled = !isLoading
        binding.loadingIndicator.isVisible = isLoading
    }
}