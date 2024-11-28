package com.voyageur.application.data.network

import com.voyageur.application.data.model.CreateTrip
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseCities
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.model.ResponsePreferences
import com.voyageur.application.data.model.ResponseRegister
import com.voyageur.application.data.model.ResponseTrip
import com.voyageur.application.data.model.UserTrip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body requestRegister: RegisterDataAccount): ResponseRegister

    @POST("auth/login")
    suspend fun login(@Body requestLogin: LoginDataAccount): ResponseLogin

    @GET("/preferences")
    fun getAllPreferences() : Response<ResponsePreferences>

    @GET("/city")
    suspend fun getAllCities() : Response<ResponseCities>

    @POST("/trips/create")
    suspend fun createTrip(
        @Body createTrip: CreateTrip
    ): Response<ResponseTrip>

    @GET("trips/user/{userId}")
    suspend fun getTripsByUserId(
        @Path("userId") userId: String
    ): Response<UserTrip>

    @GET("trips/{tripId}/detail")
    suspend fun getTripDetail(
        @Path("tripId") tripId: String
    ): Response<ResponseTrip>

    @GET("trips/{tripId}/participants")
    suspend fun getParticipants(
        @Path("tripId") tripId: String
    ): Response<UserTrip>
}