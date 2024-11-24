package com.voyageur.application.data.network

import com.voyageur.application.data.model.CreateTrip
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseCities
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.model.ResponsePreferences
import com.voyageur.application.data.model.ResponseRegister
import com.voyageur.application.data.model.ResponseTrip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body requestRegister: RegisterDataAccount): ResponseRegister

    @POST("auth/login")
    suspend fun login(@Body requestLogin: LoginDataAccount): ResponseLogin

    @GET("/preferences")
    fun getAllPreferences() : Response<ResponsePreferences>

    @GET("/city")
    fun getAllCities() : Response<ResponseCities>

    @POST("/trip/create")
    suspend fun createTrip(
        @Body createTrip: CreateTrip
    ): Response<ResponseTrip>

}