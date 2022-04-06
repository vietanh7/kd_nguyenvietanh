package com.test.demo.features.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.test.demo.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {
    protected val loading = MutableStateFlow(false)
    val isLoading = loading.asLiveData()

    protected val errorEvent = SingleLiveEvent<Throwable>()
    fun error(): LiveData<Throwable> = errorEvent

    fun launchJob(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = createExceptionHandler(),
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(context + errorHandler, start, block)
    }

    fun launchLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = createExceptionHandler(),
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(context + errorHandler, start) {
            loading.value = true
            try {
                block()
            } finally {
                loading.value = false
            }
        }
    }


    protected open fun createExceptionHandler() =
        CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                Timber.e(throwable)
                errorEvent.postValue(throwable)
            }
        }
}