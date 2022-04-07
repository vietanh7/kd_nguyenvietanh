package com.test.demo.features.product

import com.test.demo.features.base.Event

sealed class ProductEvent: Event {
    object AddSuccess: ProductEvent()
    object EditSuccess: ProductEvent()
}