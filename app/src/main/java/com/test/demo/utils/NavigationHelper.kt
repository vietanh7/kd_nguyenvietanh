package com.test.demo.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.test.demo.R

class NavigationHelper(val fm: FragmentManager, @IdRes val containerId: Int) {

    val currentFragment
        get() = fm.findFragmentById(containerId)

    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true, name: String? = null) {
        fm.commit {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if (addToBackStack) {
                addToBackStack(name)
            }
            replace(R.id.fragment_container, fragment)
        }
    }

    fun openAsRoot(fragment: Fragment) {
        for (i in 0..fm.backStackEntryCount) {
            fm.popBackStackImmediate()
        }

        changeFragment(fragment, false)
    }
}