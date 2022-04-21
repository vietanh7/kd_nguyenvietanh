package com.test.demo.product.domain

import com.test.demo.base.SingleUseCase
import com.test.demo.data.api.model.Product
import com.test.demo.data.repo.ProductRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface AddProductUseCase: SingleUseCase<Product, Product>

class AddProductUseCaseImpl @Inject constructor(private val productRepo: ProductRepo): AddProductUseCase {
    override fun execute(args: Product): Single<Product> = productRepo.addProduct(args)
}