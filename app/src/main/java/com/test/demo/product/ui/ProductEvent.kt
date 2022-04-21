package com.test.demo.product.ui

import com.test.demo.base.Event

sealed interface ProductEvent: Event {
    object AddSuccess: ProductEvent
    object EditSuccess: ProductEvent
}