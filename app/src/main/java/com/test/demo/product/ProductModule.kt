package com.test.demo.product

import com.test.demo.product.domain.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ProductModule {
    @Binds
    fun bindAddProductUseCase(impl: AddProductUseCaseImpl): AddProductUseCase

    @Binds
    fun bindUpdateProductUseCase(impl: UpdateProductUseCaseImpl): UpdateProductUseCase

    @Binds
    fun bindDeleteProductUseCase(impl: DeleteProductUseCaseImpl): DeleteProductUseCase

    @Binds
    fun bindSearchProductUseCase(impl: SearchProductUseCaseImpl): SearchProductUseCase

    @Binds
    fun bindGetProductListUseCase(impl: GetProductListUseCaseImpl): GetProductListUseCase
}