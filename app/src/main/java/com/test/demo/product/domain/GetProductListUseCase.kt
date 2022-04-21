package com.test.demo.product.domain

import com.test.demo.data.api.model.Product
import com.test.demo.data.repo.ProductRepo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface GetProductListUseCase {
    fun execute(force: Boolean): Single<List<Product>>
}

class GetProductListUseCaseImpl @Inject constructor(private val productRepo: ProductRepo): GetProductListUseCase {
    override fun execute(force: Boolean ): Single<List<Product>> = productRepo.getListProduct(force)
}