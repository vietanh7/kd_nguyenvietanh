package com.test.demo.features.product.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.demo.databinding.ProductHeaderItemBinding
import com.test.demo.utils.viewBinding

class ProductHeaderAdapter: RecyclerView.Adapter<ProductHeaderAdapter.HeaderVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderVH {
        val binding = parent.viewBinding(ProductHeaderItemBinding::inflate)
        return HeaderVH(binding.root)
    }

    override fun onBindViewHolder(holder: HeaderVH, position: Int) {

    }

    class HeaderVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}