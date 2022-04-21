package com.test.demo.auth.ui

import com.test.demo.base.Event

sealed interface AuthEvent: Event {
    object LoginSuccessEvent: AuthEvent
    object RegisterSuccessEvent: AuthEvent
}