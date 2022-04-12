package com.test.demo.utils.dispatcher

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenExpiredDispatcher @Inject constructor() {
    private val eventChannel = Channel<Unit>(Channel.UNLIMITED)
    val eventFlow = eventChannel.receiveAsFlow()

    fun dispatch() = eventChannel.trySend(Unit)
}