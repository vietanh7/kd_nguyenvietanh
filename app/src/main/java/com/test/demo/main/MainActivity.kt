package com.test.demo.main

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.test.demo.R
import com.test.demo.data.prefs.PrefsHelper
import com.test.demo.databinding.ActivityMainBinding
import com.test.demo.base.BaseActivity
import com.test.demo.utils.dispatcher.NavigationDispatcher
import com.test.demo.utils.dispatcher.TokenExpiredDispatcher
import com.test.demo.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), NavController.OnDestinationChangedListener {

    override val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var tokenExpiredDispatcher: TokenExpiredDispatcher

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    @Inject
    lateinit var prefsHelper: PrefsHelper

    private val navController
        get() = findNavController(R.id.nav_host)

    private val navHostFragment
        get() = supportFragmentManager.findFragmentById(navHostId) as NavHostFragment

    override val navHostId: Int
        get() = R.id.nav_host

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        if (navHostFragment.navController.currentDestination == null) {
            val startDestination = if (prefsHelper.getToken().isNullOrEmpty()) R.id.registerFragment else R.id.loginFragment
            initNavigation(startDestination)
        }

        handleNavigationEvent()
        handleTokenExpired()
    }

    private fun initNavigation(start: Int) {
        // https://issuetracker.google.com/issues/142847973?pli=1
        navHostFragment.apply {
            val navInflater = navController.navInflater
            val navGraph = navInflater.inflate(R.navigation.nav_graph)
            navGraph.setStartDestination(start)
            navController.graph = navGraph
            navController.addOnDestinationChangedListener(this@MainActivity)
        }
    }

    private fun handleTokenExpired() {
        lifecycleScope.launchWhenStarted {
            tokenExpiredDispatcher.eventFlow.collect {
                initNavigation(R.id.loginFragment)
                showMessage(getString(R.string.token_expired_message))
            }
        }
    }

    private fun handleNavigationEvent() {
        lifecycleScope.launchWhenResumed {
            navigationDispatcher.eventFlow.collect {
                it.invoke(navController)
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

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) = Unit
}