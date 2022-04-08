package com.test.demo.features.product.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.demo.R
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductListFragmentBinding
import com.test.demo.features.base.BaseFragment
import com.test.demo.features.product.ProductViewModel
import com.test.demo.features.product.edit.EditProductFragment
import com.test.demo.utils.hideKeyboard
import com.test.demo.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductListFragment: BaseFragment<ProductListFragmentBinding, ProductViewModel>(R.layout.product_list_fragment),
    SwipeRefreshLayout.OnRefreshListener, ProductAdapter.Callback, SearchView.OnQueryTextListener {
    override val binding: ProductListFragmentBinding by viewBinding { ProductListFragmentBinding.bind(it) }
    override val viewModel: ProductViewModel by sharedViewModel()

    var productAdapter: ProductAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeVM()
    }

    private fun setup() {
        with(binding) {
            setupToolbar(toolbar, getString(R.string.product_list_title))
            setHasOptionsMenu(true)
            refreshLayout.setOnRefreshListener(this@ProductListFragment)

            productAdapter = ProductAdapter(this@ProductListFragment)
            productRc.adapter = productAdapter
            productRc.setHasFixedSize(true)

            btnAdd.setOnClickListener { toAddProduct() }
        }
    }

    private fun observeVM() {
        viewModel.listProduct.asLiveData().observe {
            productAdapter?.submitList(it)
        }

        viewModel.needReload.observe { viewModel.getProductList() }
    }

    override fun handleLoading(isLoading: Boolean) {
        binding.refreshLayout.isRefreshing = isLoading
    }

    private fun toAddProduct() {
        viewModel.clearEditData()
        findNavController().navigate(R.id.action_productList_to_add)
    }

    private fun toEditProduct(product: Product) {
        viewModel.clearEditData()
        findNavController().navigate(R.id.action_productList_to_edit, bundleOf(EditProductFragment.PRODUCT_ARGS_KEY to product))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_list_menu, menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as? SearchView ?: return
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        searchView.setOnQueryTextFocusChangeListener { _, b ->
            if (b) {
                binding.toolbar.title = ""
            } else {
                binding.toolbar.title = getString(R.string.product_list_title)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRefresh() {
        viewModel.getProductList()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        hideKeyboard()
        viewModel.searchBySku(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    override fun deleteProductClick(product: Product) {
        viewModel.deleteProduct(product.sku)
    }

    override fun editProductClick(product: Product) {
        toEditProduct(product)
    }

    override fun onItemClick(product: Product) {
        toEditProduct(product)
    }
}