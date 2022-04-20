package com.test.demo.data.remote.api

import java.io.IOException

class ApiError(message: String, val code: Int): IOException(message) {

}