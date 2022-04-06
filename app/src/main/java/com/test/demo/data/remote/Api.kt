package com.test.demo.data.remote

import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token

interface Api {
    suspend fun login(email: String, password: String): Token

    suspend fun register(email: String, password: String): RegisterResponse
}

class ApiIml(private val apiService: ApiService): Api {
    override suspend fun login(email: String, password: String): Token {
        return apiService.login(email, password)
    }

    override suspend fun register(email: String, password: String): RegisterResponse {
        return apiService.register(email, password)
    }
}