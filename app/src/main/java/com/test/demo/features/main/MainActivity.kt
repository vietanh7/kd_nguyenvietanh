package com.test.demo.features.main

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.test.demo.R
import com.test.demo.data.local.PrefsHelper
import com.test.demo.databinding.ActivityMainBinding
import com.test.demo.dispatcher.NavigationDispatcher
import com.test.demo.dispatcher.TokenExpiredDispatcher
import com.test.demo.features.base.BaseActivity
import com.test.demo.utils.viewBinding
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>(), NavController.OnDestinationChangedListener {

    override val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }
    private val tokenExpiredDispatcher by inject<TokenExpiredDispatcher>()
    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val prefsHelper: PrefsHelper by inject()
    private val navController
        get() = findNavController(R.id.fragment_container)

    override val navHostId: Int
        get() = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        val startDestination = if (prefsHelper.getToken().isNullOrEmpty()) R.id.registerFragment else R.id.loginFragment
        initNavigation(startDestination)
        handleTokenExpired()
    }

    private fun initNavigation(start: Int) {
        // https://issuetracker.google.com/issues/142847973?pli=1
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).apply {
            val navInflater = navController.navInflater
            val navGraph = navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(start)
            navController.graph = navGraph
            navController.addOnDestinationChangedListener(this@MainActivity)
        }

        lifecycleScope.launchWhenResumed {
            navigationDispatcher.eventFlow.collect {
                it.invoke(navController)
            }
        }
    }

    private fun handleTokenExpired() {
        lifecycleScope.launchWhenStarted {
            tokenExpiredDispatcher.eventFlow.collect {
                initNavigation(R.id.loginFragment)
                navController.clearBackStack(R.id.loginFragment)
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

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

    }
}