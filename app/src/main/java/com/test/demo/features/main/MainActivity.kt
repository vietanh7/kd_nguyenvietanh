package com.test.demo.features.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.test.demo.R
import com.test.demo.databinding.ActivityMainBinding
import com.test.demo.features.base.BaseActivity
import com.test.demo.features.login.LoginFragment
import com.test.demo.utils.viewBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        val fragment = supportFragmentManager.findFragmentById(R .id.fragment_container) ?: LoginFragment.newInstance()
        changeFragment(fragment, false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true, name: String? = null) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                addToBackStack(name)
            }
        }
    }
}