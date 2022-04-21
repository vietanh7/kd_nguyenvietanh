package com.test.demo.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.test.demo.utils.SingleLiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    protected val isLoading = MutableStateFlow(false)
    val loading = isLoading.asLiveData()

    protected val errorEvent = SingleLiveEvent<Throwable>()
    fun error(): LiveData<Throwable> = errorEvent

    protected val event = SingleLiveEvent<Event>()
    fun event(): LiveData<Event> = event

    protected val compositeDisposable = CompositeDisposable()

    fun Disposable.addToCompositeDisposable(): Boolean {
        return compositeDisposable.add(this)
    }

    fun <T : Any> Single<T>.bindLoading(): Single<T> {
        return this.doOnSubscribe { isLoading.value = true }
            .doAfterTerminate { isLoading.value = false }
    }

    fun Completable.bindLoading(): Completable {
        return this.doOnSubscribe { isLoading.value = true }
            .doAfterTerminate { isLoading.value = false }
    }

    open fun handleError(error: Throwable) {
        errorEvent.postValue(error)
    }

    protected open fun createExceptionHandler() =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                Timber.e(throwable)
                errorEvent.postValue(throwable)
            }
        }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}