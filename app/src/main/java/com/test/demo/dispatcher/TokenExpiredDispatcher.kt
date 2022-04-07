package com.test.demo.dispatcher

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TokenExpiredDispatcher {
    private val eventFlow = MutableSharedFlow<Unit>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    fun eventFlow(): SharedFlow<Unit> = eventFlow

    fun dispatch() {
        eventFlow.tryEmit(Unit)
    }
}