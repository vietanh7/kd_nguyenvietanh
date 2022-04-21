package com.test.demo.product.ui.edit

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.test.demo.R
import com.test.demo.base.Event
import com.test.demo.product.ui.ProductEvent
import com.test.demo.product.ui.add.AddProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment: AddProductFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        with(binding) {
            toolbar.title = getString(R.string.edit_product_title)
            actionBtn.text = getString(R.string.update_action)
            textFieldSku.isEnabled = false
            actionBtn.setOnClickListener { viewModel.editProduct() }
        }
    }

    override fun onNewEvent(event: Event) {
        when(event) {
            is ProductEvent.EditSuccess -> {
                showSnackbar(getString(R.string.update_product_success_greeting))
                findNavController().navigateUp()
            }

            else -> super.onNewEvent(event)
        }
    }

    companion object {
        const val PRODUCT_ARGS_KEY = "PRODUCT"
    }
}