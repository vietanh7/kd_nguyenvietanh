package com.test.demo.utils.dispatcher

import org.koin.dsl.module

val dispatcherModule
    get() = module {
        single { TokenExpiredDispatcher() }
        single { NavigationDispatcher() }
    }