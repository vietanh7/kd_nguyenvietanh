package com.test.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.test.demo.data.api.model.Product
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals

fun<T> LiveData<T>.observeAndGet(): T? {
    val observer = Observer<T> {  }
    observeForever(observer)
    return value
}

fun getRandomProduct(): Product {
    val uuid = UUID.randomUUID().toString()
    return Product(
        createdAt = "",
        updatedAt = "",
        productName = uuid.takeLast(5),
        price = Random.nextInt(0, 4000).toLong(),
        unit = "Carbon",
        qty = Random.nextInt(0, 400),
        sku = uuid.takeLast(4),
        status = 1,
        success = true,
        message = "",
        image = null,
        id = -1
    )
}

fun assertProduct(expected: Product, actual: Product?) {
    assertEquals(expected.sku, actual?.sku)
    assertEquals(expected.productName, actual?.productName)
    assertEquals(expected.qty, actual?.qty)
    assertEquals(expected.price, actual?.price)
    assertEquals(expected.unit, actual?.unit)
    assertEquals(expected.status, actual?.status)
}