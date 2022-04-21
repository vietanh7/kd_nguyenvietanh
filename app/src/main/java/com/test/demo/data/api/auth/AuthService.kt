package com.test.demo.data.api.auth

import com.test.demo.data.api.common.ApiConstants
import com.test.demo.data.api.model.RegisterResponse
import com.test.demo.data.api.model.Token
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