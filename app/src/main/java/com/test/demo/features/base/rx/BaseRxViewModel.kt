package com.test.demo.features.base.rx

import com.test.demo.features.base.BaseViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseRxViewModel: BaseViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    fun Disposable.addToCompositeDisposable(): Boolean {
        return compositeDisposable.add(this)
    }

    fun <T : Any> Single<T>.bindLoading(): Single<T> {
        return this.doOnSubscribe { isLoading.value = true }
            .doOnSuccess { isLoading.value = false }
            .doOnError { isLoading.value = false }
    }

    open fun handleError(error: Throwable) {
        errorEvent.postValue(error)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}