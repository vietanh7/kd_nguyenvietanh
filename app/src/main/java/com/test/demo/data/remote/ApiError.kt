package com.test.demo.data.remote

import java.io.IOException

class ApiError(message: String, val code: Int): IOException(message) {

}