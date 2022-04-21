package com.test.demo.auth.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.test.demo.R
import com.test.demo.databinding.LoginFragmentBinding
import com.test.demo.base.BaseFragment
import com.test.demo.utils.setTextIfChanged
import com.test.demo.utils.throttleClick
import com.test.demo.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment<LoginFragmentBinding, AuthViewModel>(R.layout.login_fragment) {
    override val binding by viewBinding { LoginFragmentBinding.bind(it) }
    override val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVm()
    }

    private fun observeVm() {
        with(viewModel) {
            emailLiveData.observe { binding.emailEdt.setTextIfChanged(it)}
            passwordLiveData.observe { binding.passwordEdt.setTextIfChanged(it) }
            isOk.observe { binding.actionBtn.isEnabled = it }
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        with(binding) {
            actionBtn.isEnabled = !isLoading
            loadingIndicator.isVisible = isLoading
        }
    }

    private fun setup() {
        with(binding) {
            title.setText(R.string.login)
            actionBtn.setText(R.string.login)
            navigationText.setText(R.string.register)
            actionBtn.setOnClickListener { viewModel.login() }
            emailEdt.doAfterTextChanged { viewModel.setEmail(it?.toString()) }
            passwordEdt.doAfterTextChanged { viewModel.setPassword(it?.toString()) }
            navigationText.throttleClick {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
}