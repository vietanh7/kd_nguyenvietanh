package com.test.demo.product.domain

import com.test.demo.data.api.model.Product
import com.test.demo.data.repo.ProductRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface UpdateProductUseCase {
    fun execute(product: Product): Single<Product>
}

class UpdateProductUseCaseImpl @Inject constructor(private val productRepo: ProductRepo): UpdateProductUseCase {
    override fun execute(product: Product): Single<Product> = productRepo.updateProduct(product)
}