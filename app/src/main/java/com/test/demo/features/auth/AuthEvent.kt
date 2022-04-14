package com.test.demo.features.auth

import com.test.demo.features.base.Event

sealed interface AuthEvent: Event {
    object LoginSuccessEvent: AuthEvent
    object RegisterSuccessEvent: AuthEvent
}