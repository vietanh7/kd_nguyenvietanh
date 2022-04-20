package com.test.demo.features.product.list

import androidx.recyclerview.widget.RecyclerView
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductItemBinding

class ProductItemVH(val binding: ProductItemBinding, val callback: ProductAdapter.Callback) :
    RecyclerView.ViewHolder(binding.root) {
    private var boundItem: Product? = null

    init {
        itemView.setOnClickListener {
            callback.onItemClick(boundItem ?: return@setOnClickListener)
        }

        binding.btnDelete.setOnClickListener {
            callback.deleteProductClick(boundItem ?: return@setOnClickListener)
        }

        binding.btnEdit.setOnClickListener {
            callback.editProductClick(boundItem ?: return@setOnClickListener)
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