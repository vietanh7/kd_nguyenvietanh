package com.test.demo.product.domain

import com.test.demo.data.api.model.Product
import com.test.demo.data.repo.ProductRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface DeleteProductUseCase {
    fun execute(sku: String): Single<Product>
}

class DeleteProductUseCaseImpl @Inject constructor(private val productRepo: ProductRepo): DeleteProductUseCase {
    override fun execute(sku: String): Single<Product> = productRepo.deleteProduct(sku)
}