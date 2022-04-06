package com.test.demo.features.product.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.demo.R
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductListFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.main.MainActivity
import com.test.demo.features.product.add.AddProductFragment
import com.test.demo.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment: BaseFragment<ProductListFragmentBinding, ProductListViewModel>(R.layout.product_list_fragment),
    SwipeRefreshLayout.OnRefreshListener, ProductAdapter.Callback {
    override val binding: ProductListFragmentBinding by viewBinding { ProductListFragmentBinding.bind(it) }
    override val viewModel: ProductListViewModel by viewModel()

    var productAdapter: ProductAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVM()
    }

    private fun setup() {
        with(binding) {
            setupToolbar(toolbar)
            refreshLayout.setOnRefreshListener(this@ProductListFragment)

            productAdapter = ProductAdapter(this@ProductListFragment)
            productRc.adapter = productAdapter
            productRc.setHasFixedSize(true)

            btnAdd.setOnClickListener { toAddProduct() }
        }
    }

    private fun observeVM() {
        viewModel.loading.observe { binding.refreshLayout.isRefreshing = it }
        viewModel.listProduct.asLiveData().observe {
            productAdapter?.submitList(it)
        }
    }

    private fun toAddProduct() {
        val mainActivity = activity as? MainActivity ?: return
        mainActivity.changeFragment(AddProductFragment.newInstance(), true)
    }

    override fun onRefresh() {
        viewModel.getProductList()
    }

    override fun deleteProductClick(product: Product) {
        viewModel.deleteProduct(product.sku)
    }

    override fun editProductClick(product: Product) {

    }

    override fun onItemClick(product: Product) {

    }

    companion object {
        fun newInstance(): ProductListFragment {
            return ProductListFragment()
        }
    }
}