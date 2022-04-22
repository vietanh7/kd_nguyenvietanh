package com.test.demo.product.ui.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.demo.R
import com.test.demo.data.api.model.Product
import com.test.demo.databinding.ProductListFragmentBinding
import com.test.demo.base.BaseFragment
import com.test.demo.product.ui.ProductViewModel
import com.test.demo.product.ui.edit.EditProductFragment
import com.test.demo.utils.hideKeyboard
import com.test.demo.utils.throttleClick
import com.test.demo.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment: BaseFragment<ProductListFragmentBinding, ProductViewModel>(R.layout.product_list_fragment),
    SwipeRefreshLayout.OnRefreshListener, ProductAdapter.Callback, SearchView.OnQueryTextListener {
    override val binding by viewBinding(ProductListFragmentBinding::bind)
    override val viewModel: ProductViewModel by activityViewModels()

    private var productAdapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearError()
    }

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
            productRc.adapter = ConcatAdapter(ProductHeaderAdapter(), productAdapter)
            productRc.setHasFixedSize(true)

            btnAdd.throttleClick { toAddProduct() }
            btnAdd.doOnLayout {
                productRc.updatePadding(bottom = root.height - it.top)
            }
        }
    }

    private fun observeVM() {
        viewModel.listProductLiveData.observe {
            productAdapter?.submitList(it)
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        binding.refreshLayout.isRefreshing = isLoading
    }

    private fun toAddProduct() {
        viewModel.setState(Product.empty())
        findNavController().navigate(R.id.action_productList_to_add)
    }

    private fun toEditProduct(product: Product) {
        viewModel.setState(product)
        findNavController().navigate(R.id.action_productList_to_edit, bundleOf(EditProductFragment.PRODUCT_ARGS_KEY to product))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchView = menu.findItem(R.id.search_menu).actionView as? SearchView
        searchView?.apply {
            maxWidth = Int.MAX_VALUE
            setOnQueryTextListener(this@ProductListFragment)
            setOnQueryTextFocusChangeListener { _, b ->
                if (b) {
                    binding.toolbar.title = ""
                } else {
                    binding.toolbar.title = getString(R.string.product_list_title)
                }
            }
        }
    }

    override fun onRefresh() {
        viewModel.getProductList()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        hideKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.search(newText)
        return true
    }

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