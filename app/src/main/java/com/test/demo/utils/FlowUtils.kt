package com.test.demo.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.StateFlow

fun <T> StateFlow<T>.observeAsLiveData(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    this.asLiveData().observe(lifecycleOwner, observer)
}