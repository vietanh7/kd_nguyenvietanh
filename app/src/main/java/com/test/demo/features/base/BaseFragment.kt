package com.test.demo.features.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.demo.R
import com.test.demo.utils.getThemeColor

abstract class BaseFragment<B: ViewBinding, V: BaseViewModel>(layoutRes: Int): Fragment(layoutRes) {
    abstract val binding: B
    abstract val viewModel: V

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVM()
    }

    private fun observeVM() {
        viewModel.event().observe(viewLifecycleOwner) {
            onNewEvent(it)
        }

        viewModel.error().observe { showMessage(it.message.orEmpty()) }
        viewModel.loading.observe(viewLifecycleOwner, this::handleLoading)
    }

    open fun onNewEvent(event: Event) = Unit

    open fun handleLoading(isLoading: Boolean) = Unit

    fun setupToolbar(toolbar: Toolbar, title: String? = null) {
        val activity = activity as? BaseActivity<*> ?: return
        val entryCount = if (activity.navHostId == null) {
            activity.supportFragmentManager.backStackEntryCount
        } else {
            activity.supportFragmentManager.findFragmentById(R.id.fragment_container)
                ?.childFragmentManager?.backStackEntryCount ?: 0
        }

        val displayBack = entryCount > 0

        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.apply {
            if (title != null) {
                setTitle(title)
            }

            setDisplayShowHomeEnabled(displayBack)
            setDisplayHomeAsUpEnabled(displayBack)
        }
    }

    open fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Message")
            .setMessage(message)
            .setNeutralButton("Ok") { _, _ -> }
            .show()
    }

    fun showMessageAndDo(message: String, action: Runnable) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Message")
            .setMessage(message)
            .setNeutralButton("Ok") { _, _ -> action.run() }
            .show()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setTextColor(requireContext().getThemeColor(com.google.android.material.R.attr.colorOnPrimary))
            .setBackgroundTint(requireContext().getThemeColor(com.google.android.material.R.attr.colorPrimary))
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun <T> hostActivity(): T? {
        return activity as? T
    }

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }
}