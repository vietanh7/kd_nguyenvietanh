package com.test.demo.features.base

import android.os.Bundle
import android.view.View
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
    }

    open fun onNewEvent(event: Event) {

    }

    fun setupToolbar(toolbar: Toolbar) {
        val activity = activity as? BaseActivity<*> ?: return
        val displayBack = activity.supportFragmentManager.backStackEntryCount > 1
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.apply {
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

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }
}