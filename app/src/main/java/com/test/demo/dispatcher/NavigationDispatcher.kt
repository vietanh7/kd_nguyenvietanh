package com.test.demo.dispatcher

import androidx.navigation.NavController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

typealias NavigationEvent = (NavController) -> Unit

class NavigationDispatcher {
    private val navigationChannel = Channel<NavigationEvent>(Channel.UNLIMITED)
    val eventFlow = navigationChannel.receiveAsFlow()

    fun dispatch(event: NavigationEvent) = navigationChannel.trySend(event)
}