package com.test.demo.features.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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
    }

    open fun onNewEvent(event: Event) {

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