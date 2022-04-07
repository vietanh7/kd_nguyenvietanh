package com.test.demo.dispatcher

import org.koin.dsl.module

val dispatcherModule
    get() = module {
        single { TokenExpiredDispatcher() }
    }