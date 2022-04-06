package com.test.demo.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Ref: https://gist.github.com/gmk57/aefa53e9736d4d4fb2284596fb62710d

fun <V : ViewBinding> Fragment.viewBinding(creator: (View) -> V): FragmentBindingDelegate<V> {
    return FragmentBindingDelegate(this, creator)
}

inline fun <V : ViewBinding> Activity.viewBinding(crossinline creator: (LayoutInflater) -> V) =
    lazy(LazyThreadSafetyMode.NONE) { creator(layoutInflater) }

inline fun <T : ViewBinding> ViewGroup.viewBinding(factory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    factory(LayoutInflater.from(context), this, false)



/** Binding delegate for DialogFragments implementing onCreateDialog (like Activities, they don't
 *  have a separate view lifecycle), may be used since onCreateDialog up to onDestroy (inclusive) */
inline fun <T : ViewBinding> DialogFragment.viewBinding(crossinline factory: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(layoutInflater)
    }

class FragmentBindingDelegate<V : ViewBinding>(
    private val host: Fragment,
    private val creator: (View) -> V
): ReadOnlyProperty<Fragment, V>, DefaultLifecycleObserver {

    private var binding: V? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        return binding ?: createBinding(thisRef)
    }

    private fun createBinding(fragment: Fragment): V {
        return binding ?: creator(host.requireView()).also {
            // if binding is accessed after Lifecycle is DESTROYED, create new instance, but don't cache it
            if (host.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                host.viewLifecycleOwner.lifecycle.addObserver(this)
                binding = it
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        binding = null
    }
}
