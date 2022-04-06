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
    protected val isLoading = MutableStateFlow(false)
    val loading = isLoading.asLiveData()

    protected val errorEvent = SingleLiveEvent<Throwable>()
    fun error(): LiveData<Throwable> = errorEvent

    protected val event = SingleLiveEvent<Event>()
    fun event(): LiveData<Event> = event

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
            isLoading.value = true
            try {
                block()
            } finally {
                isLoading.value = false
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