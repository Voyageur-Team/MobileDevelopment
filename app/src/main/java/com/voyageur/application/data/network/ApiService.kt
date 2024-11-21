package com.voyageur.application.data.network

import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.model.ResponseRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/signup")
    suspend fun register(@Body requestRegister: RegisterDataAccount): ResponseRegister

    @POST("auth/login")
    suspend fun login(@Body requestLogin: LoginDataAccount): ResponseLogin
}