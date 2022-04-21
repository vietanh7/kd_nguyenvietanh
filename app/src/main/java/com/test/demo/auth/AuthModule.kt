package com.test.demo.auth

import com.test.demo.auth.domain.LoginUseCase
import com.test.demo.auth.domain.LoginUseCaseImpl
import com.test.demo.auth.domain.RegisterUseCase
import com.test.demo.auth.domain.RegisterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun bindRegisterUseCase(impl: RegisterUseCaseImpl): RegisterUseCase
}