package com.test.demo.utils

fun <T: Any?> T.toStringOrEmpty(): String {
    return this?.toString().orEmpty()
}

fun String?.toIntOr(default: Int): Int {
    if (this == null) {
        return default
    }

    return this.toIntOrNull() ?: default
}

fun String?.asBearerToken(): String {
    return "Bearer ${this.orEmpty()}"
}