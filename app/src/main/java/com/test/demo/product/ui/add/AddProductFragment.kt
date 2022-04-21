package com.test.demo.product.ui.add

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.test.demo.R
import com.test.demo.data.api.model.Product
import com.test.demo.databinding.AddProductFragmentBinding
import com.test.demo.base.BaseFragment
import com.test.demo.base.Event
import com.test.demo.product.ui.ProductEvent
import com.test.demo.product.ui.ProductViewModel
import com.test.demo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AddProductFragment :
    BaseFragment<AddProductFragmentBinding, ProductViewModel>(R.layout.add_product_fragment) {
    override val binding by viewBinding (AddProductFragmentBinding::bind)
    override val viewModel: ProductViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVM()
    }

    private fun setup() {
        setupToolbar(binding.toolbar, getString(R.string.add_product))
        with(binding) {
            skuText.doAfterTextChanged { updateState { copy(sku = it.toStringOrEmpty()) } }
            productName.doAfterTextChanged { updateState { copy(productName = it.toStringOrEmpty()) } }
            quantity.doAfterTextChanged { updateState { copy(qty = it.toString().toIntOr(0)) } }
            price.doAfterTextChanged { updateState { copy(price = it.toString().toLongOrNull() ?: 0) } }
            productUnit.doAfterTextChanged { updateState { copy(unit = it.toStringOrEmpty()) } }
            productStatus.doAfterTextChanged { updateState { copy(status = it.toString().toIntOr(1)) } }

            actionBtn.setOnClickListener { viewModel.addProduct() }
        }
    }

    private inline fun updateState(block: Product.() -> Product) {
        val currentProduct = viewModel.productStateLiveData.value ?: return
        viewModel.setState(currentProduct.block())
    }

    private fun observeVM() {
        viewModel.productStateLiveData.observe {
            with(binding) {
                skuText.setTextIfChanged(it.sku)
                productName.setTextIfChanged(it.productName)
                quantity.setTextIfChanged(it.qty.toString())
                price.setTextIfChanged(it.price.toString())
                productUnit.setTextIfChanged(it.unit)
                productStatus.setTextIfChanged(it.status.toString())
            }
        }
    }


    override fun handleLoading(isLoading: Boolean) {
        with(binding) {
            progressBar.isInvisible = !isLoading
            actionBtn.isEnabled = !isLoading
        }
    }

    override fun onNewEvent(event: Event) {
        when (event) {
            is ProductEvent.AddSuccess -> {
                showSnackbar(getString(R.string.add_product_success_greeting))
                findNavController().navigateUp()
            }

            else -> super.onNewEvent(event)
        }
    }
}