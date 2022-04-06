package com.test.demo.features.login

import android.os.Bundle
import android.view.View
import com.test.demo.R
import com.test.demo.databinding.LoginFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.utils.viewBinding

class LoginFragment: BaseFragment<LoginFragmentBinding>(R.layout.login_fragment) {
    override val binding by viewBinding { LoginFragmentBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}