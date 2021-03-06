package com.test.demo.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T> {
    private val mPending = AtomicBoolean(false)

    constructor(): super()

    constructor(value: T): super(value) {
        mPending.set(true)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    // Post value call setValue internally so don't need to override post value
    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }
}
