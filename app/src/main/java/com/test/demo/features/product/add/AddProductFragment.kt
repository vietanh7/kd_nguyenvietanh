package com.test.demo.features.product.add

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.test.demo.R
import com.test.demo.databinding.AddProductFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.base.Event
import com.test.demo.features.product.ProductEvent
import com.test.demo.features.product.ProductViewModel
import com.test.demo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class AddProductFragment: BaseFragment<AddProductFragmentBinding, ProductViewModel>(R.layout.add_product_fragment) {
    override val binding: AddProductFragmentBinding by viewBinding { AddProductFragmentBinding.bind(it) }
    override val viewModel: ProductViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVM()
    }

    private fun setup() {
        setupToolbar(binding.toolbar, getString(R.string.add_product))
        with(binding) {
            skuText.doAfterTextChanged { viewModel.sku.value = it.toStringOrEmpty() }
            productName.doAfterTextChanged { viewModel.productName.value = it.toStringOrEmpty() }
            quantity.doAfterTextChanged { viewModel.quantity.value = it.toString().toIntOr(0) }
            price.doAfterTextChanged { viewModel.price.value = it.toString().toIntOr(0) }
            productUnit.doAfterTextChanged { viewModel.unit.value = it.toStringOrEmpty() }
            productStatus.doAfterTextChanged { viewModel.status.value = it.toString().toIntOr(1) }

            btnAdd.setOnClickListener { viewModel.addProduct() }
        }
    }

    private fun observeVM() {
        viewModel.sku.observeAsLiveData(viewLifecycleOwner) { binding.skuText.setTextIfChanged(it) }
        viewModel.productName.observeAsLiveData(viewLifecycleOwner) { binding.productName.setTextIfChanged(it) }
        viewModel.quantity.observeAsLiveData(viewLifecycleOwner) { binding.quantity.setTextIfChanged(it.toString()) }
        viewModel.price.observeAsLiveData(viewLifecycleOwner) { binding.price.setTextIfChanged(it.toString()) }
        viewModel.unit.observeAsLiveData(viewLifecycleOwner) { binding.productUnit.setTextIfChanged(it) }
        viewModel.status.observeAsLiveData(viewLifecycleOwner) { binding.productStatus.setTextIfChanged(it.toString()) }
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