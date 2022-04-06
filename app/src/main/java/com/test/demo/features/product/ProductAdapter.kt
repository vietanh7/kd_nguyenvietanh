package com.test.demo.features.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.test.demo.data.remote.model.Product
import com.test.demo.databinding.ProductItemBinding

class ProductAdapter(val callback: Callback): ListAdapter<Product, ProductItemVH>(DiffItemCallback()) {

    class DiffItemCallback: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemVH {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductItemVH(binding, callback)
    }

    override fun onBindViewHolder(holder: ProductItemVH, position: Int) {
        holder.bind(getItem(position))
    }

    interface Callback {
        fun deleteProductClick(product: Product)
        fun editProductClick(product: Product)
        fun onItemClick(product: Product)
    }
}