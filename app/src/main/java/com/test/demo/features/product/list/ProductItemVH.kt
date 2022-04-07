package com.test.demo.features.product.list

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductItemBinding
import com.test.demo.features.product.list.ProductAdapter

class ProductItemVH(val binding: ProductItemBinding, val callback: ProductAdapter.Callback) :
    RecyclerView.ViewHolder(binding.root) {
    var boundItem: Product? = null

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
            headerGroup.isGone = adapterPosition != 0
        }
    }
}