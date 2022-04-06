package com.test.demo.features.login

import com.test.demo.features.base.Event

sealed class LoginEvent: Event {
    object LoginSuccessEvent: LoginEvent()
}