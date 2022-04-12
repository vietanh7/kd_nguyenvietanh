package com.test.demo.features.product.edit

import android.os.Bundle
import android.view.View
import com.test.demo.R
import com.test.demo.data.remote.model.Product
import com.test.demo.features.base.Event
import com.test.demo.features.product.ProductEvent
import com.test.demo.features.product.add.AddProductFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment: AddProductFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        initData()
        with(binding) {
            toolbar.title = getString(R.string.edit_product_title)
            btnAdd.text = getString(R.string.update_action)
            textFieldSku.isEnabled = false
            btnAdd.setOnClickListener { viewModel.editProduct() }
        }
    }

    private fun initData() {
        val product = arguments?.getParcelable<Product>(PRODUCT_ARGS_KEY) ?: return
        viewModel.init(product)
    }

    override fun onNewEvent(event: Event) {
        when(event) {
            is ProductEvent.EditSuccess -> {
                showMessage(getString(R.string.update_product_success_greeting))
            }

            else -> super.onNewEvent(event)
        }
    }

    companion object {
        const val PRODUCT_ARGS_KEY = "PRODUCT"

        fun newInstance(product: Product): EditProductFragment {
            val args = Bundle(1).apply {
                putParcelable(PRODUCT_ARGS_KEY, product)
            }
            val fragment = EditProductFragment()
            fragment.arguments = args
            return fragment
        }
    }
}