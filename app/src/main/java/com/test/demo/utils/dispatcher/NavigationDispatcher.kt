package com.test.demo.utils.dispatcher

import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

typealias NavigationEvent = (NavController) -> Unit

@ActivityRetainedScoped
class NavigationDispatcher @Inject constructor() {
    private val navigationChannel = Channel<NavigationEvent>(Channel.UNLIMITED)
    val eventFlow = navigationChannel.receiveAsFlow()

    fun dispatch(event: NavigationEvent) = navigationChannel.trySend(event)
}