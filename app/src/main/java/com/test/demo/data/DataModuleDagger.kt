package com.test.demo.data

import com.test.demo.data.remote.Api
import com.test.demo.data.remote.ApiIml
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleDagger {

    @Binds
    abstract fun bindApiImpl(impl: ApiIml): Api
}