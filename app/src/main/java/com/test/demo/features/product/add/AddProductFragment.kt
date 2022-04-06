package com.test.demo.features.product.add

import android.os.Bundle
import android.view.View
import com.test.demo.R
import com.test.demo.databinding.AddProductFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddProductFragment: BaseFragment<AddProductFragmentBinding, AddProductViewModel>(R.layout.add_product_fragment) {
    override val binding: AddProductFragmentBinding by viewBinding { AddProductFragmentBinding.bind(it) }
    override val viewModel: AddProductViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        setupToolbar(binding.toolbar)
    }

    private fun observeVM() {

    }

    companion object {
        fun newInstance(): AddProductFragment {
            return AddProductFragment()
        }
    }
}