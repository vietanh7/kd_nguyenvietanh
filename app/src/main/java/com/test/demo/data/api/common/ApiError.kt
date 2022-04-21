package com.test.demo.data.api.common

import java.io.IOException

class ApiError(message: String, val code: Int): IOException(message) {

}