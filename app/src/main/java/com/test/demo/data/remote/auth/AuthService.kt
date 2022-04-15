package com.test.demo.data.remote.auth

import com.test.demo.data.remote.api.ApiConstants
import com.test.demo.data.remote.model.RegisterResponse
import com.test.demo.data.remote.model.Token
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_ENDPOINT)
    fun login(@Field("email") email: String, @Field("password") password: String): Single<Token>

    @FormUrlEncoded
    @POST(ApiConstants.REGISTER_ENDPOINT)
    fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<RegisterResponse>
}