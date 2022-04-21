package com.test.demo.product.domain

import com.test.demo.base.SingleUseCase
import com.test.demo.data.api.model.Product
import com.test.demo.data.repo.ProductRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface SearchProductUseCase: SingleUseCase<String, Product>

class SearchProductUseCaseImpl @Inject constructor(private val productRepo: ProductRepo): SearchProductUseCase {
    override fun execute(args: String): Single<Product> = productRepo.searchProduct(args)
}