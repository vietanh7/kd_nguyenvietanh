package com.test.demo.features.product.list

import androidx.recyclerview.widget.RecyclerView
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductItemBinding
import com.test.demo.utils.throttleClick

class ProductItemVH(val binding: ProductItemBinding, val callback: ProductAdapter.Callback) :
    RecyclerView.ViewHolder(binding.root) {
    private var boundItem: Product? = null

    init {
        itemView.throttleClick {
            callback.onItemClick(boundItem ?: return@throttleClick)
        }

        binding.btnDelete.throttleClick {
            callback.deleteProductClick(boundItem ?: return@throttleClick)
        }

        binding.btnEdit.throttleClick {
            callback.editProductClick(boundItem ?: return@throttleClick)
        }
    }

    fun bind(product: Product) {
        boundItem = product
        with(binding) {
            productName.text = product.productName
            sku.text = product.sku
        }
    }
}