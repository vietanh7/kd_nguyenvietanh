package com.test.demo.base

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface UseCase<I, O> {
    fun execute(args: I): O
}

interface SingleUseCase<I, O : Any>: UseCase<I, Single<O>>

interface CompletableUseCase<I>: UseCase<I, Completable>

interface ObservableUseCase<I, O: Any>: UseCase<I, Observable<O>>