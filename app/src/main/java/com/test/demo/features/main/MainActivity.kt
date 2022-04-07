package com.test.demo.features.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.test.demo.R
import com.test.demo.data.local.PrefsHelper
import com.test.demo.databinding.ActivityMainBinding
import com.test.demo.dispatcher.TokenExpiredDispatcher
import com.test.demo.features.base.BaseActivity
import com.test.demo.features.auth.LoginFragment
import com.test.demo.features.auth.RegisterFragment
import com.test.demo.utils.NavigationHelper
import com.test.demo.utils.viewBinding
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }
    val navigationHelper by lazy { NavigationHelper(supportFragmentManager, R.id.fragment_container) }
    private val tokenExpiredDispatcher by inject<TokenExpiredDispatcher>()
    private val prefsHelper: PrefsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        val fragment = navigationHelper.currentFragment ?: getStartDestination()
        navigationHelper.changeFragment(fragment, false)
        handleTokenExpired()
    }

    private fun getStartDestination(): Fragment {
        return if (prefsHelper.getToken().isNullOrEmpty()) {
            RegisterFragment.newInstance()
        } else {
            LoginFragment.newInstance()
        }
    }

    private fun handleTokenExpired() {
        lifecycleScope.launchWhenStarted {
            tokenExpiredDispatcher.eventFlow().collect {
                navigationHelper.openAsRoot(LoginFragment.newInstance())
                showMessage(getString(R.string.token_expired_message))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true, name: String? = null) {
        navigationHelper.changeFragment(fragment, addToBackStack, name)
    }
}