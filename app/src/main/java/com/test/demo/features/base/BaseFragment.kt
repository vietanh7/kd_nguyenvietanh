package com.test.demo.features.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding

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
        val displayBack = activity.supportFragmentManager.backStackEntryCount > 0
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
        AlertDialog.Builder(requireContext())
            .setTitle("Message")
            .setMessage(message)
            .setNeutralButton("Ok") { _, _ -> }
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }
}