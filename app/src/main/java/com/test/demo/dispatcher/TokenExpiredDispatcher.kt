package com.test.demo.dispatcher

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class TokenExpiredDispatcher {
    private val eventChannel = Channel<Unit>(Channel.UNLIMITED)
    val eventFlow = eventChannel.receiveAsFlow()

    fun dispatch() = eventChannel.trySend(Unit)
}