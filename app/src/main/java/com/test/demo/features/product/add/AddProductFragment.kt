package com.test.demo.features.product.add

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.test.demo.R
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.AddProductFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.base.Event
import com.test.demo.features.product.ProductEvent
import com.test.demo.features.product.ProductViewModel
import com.test.demo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AddProductFragment :
    BaseFragment<AddProductFragmentBinding, ProductViewModel>(R.layout.add_product_fragment) {
    override val binding: AddProductFragmentBinding by viewBinding {
        AddProductFragmentBinding.bind(
            it
        )
    }
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

            btnAdd.setOnClickListener { viewModel.addProduct() }
        }
    }

    private inline fun updateState(block: Product.() -> Product) {
        val currentProduct = viewModel.productStateLiveData.value ?: return
        viewModel.setSate(currentProduct.block())
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
        binding.progressBar.isInvisible = !isLoading
        binding.btnAdd.isEnabled = !isLoading
    }

    override fun onNewEvent(event: Event) {
        when (event) {
            is ProductEvent.AddSuccess -> {
                showMessage(getString(R.string.add_product_success_greeting))
            }

            else -> super.onNewEvent(event)
        }
    }

    companion object {
        fun newInstance(): AddProductFragment {
            return AddProductFragment()
        }
    }
}