package com.test.demo.data

import android.content.Context
import com.test.demo.data.db.AppDb
import com.test.demo.data.prefs.PrefsHelper
import com.test.demo.data.prefs.PrefsHelperImpl
import com.test.demo.data.api.auth.AuthApi
import com.test.demo.data.api.auth.AuthApiImpl
import com.test.demo.data.api.auth.AuthService
import com.test.demo.data.api.product.ProductApi
import com.test.demo.data.api.product.ProductApiImpl
import com.test.demo.data.api.product.ProductApiService
import com.test.demo.data.repo.ProductRepo
import com.test.demo.data.repo.ProductRepoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindAuthApi(impl: AuthApiImpl): AuthApi

    @Binds
    abstract fun bindProductApi(impl: ProductApiImpl): ProductApi

    @Binds
    abstract fun bindPrefsHelper(impl: PrefsHelperImpl): PrefsHelper

    @Binds
    abstract fun bindProductRepo(impl: ProductRepoImpl): ProductRepo

    companion object {
        @Singleton
        @Provides
        fun provideAuthService(retrofit: Retrofit): AuthService {
            return retrofit.create(AuthService::class.java)
        }

        @Singleton
        @Provides
        fun provideProductService(retrofit: Retrofit): ProductApiService {
            return retrofit.create(ProductApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideDb(@ApplicationContext context: Context): AppDb {
            return AppDb.provideDb(context)
        }

        @Provides
        fun provideProductDao(db: AppDb) = db.productDao()
    }
}