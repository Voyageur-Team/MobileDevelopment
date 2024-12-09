package com.voyageur.application.data.network

import com.voyageur.application.data.model.CreateTrip
import com.voyageur.application.data.model.DataParticipantPreferences
import com.voyageur.application.data.model.GetRecommendation
import com.voyageur.application.data.model.LoginDataAccount
import com.voyageur.application.data.model.ParticipantPreferences
import com.voyageur.application.data.model.Participants
import com.voyageur.application.data.model.PostMostPreferences
import com.voyageur.application.data.model.PostRecommendations
import com.voyageur.application.data.model.RegisterDataAccount
import com.voyageur.application.data.model.ResponseCities
import com.voyageur.application.data.model.ResponseItenerary
import com.voyageur.application.data.model.ResponseLogin
import com.voyageur.application.data.model.ResponseParticipants
import com.voyageur.application.data.model.ResponsePreferences
import com.voyageur.application.data.model.ResponseRegister
import com.voyageur.application.data.model.ResponseTrip
import com.voyageur.application.data.model.ResponseUserEmail
import com.voyageur.application.data.model.ResponseVote
import com.voyageur.application.data.model.UserTrip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body requestRegister: RegisterDataAccount): ResponseRegister

    @POST("auth/login")
    suspend fun login(@Body requestLogin: LoginDataAccount): Response<ResponseLogin>

    @GET("/preferences")
    suspend fun getAllPreferences() : Response<ResponsePreferences>

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

    @GET("auth/search")
    suspend fun searchUserByEmail(
        @Query("email") email: String
    ): Response<ResponseUserEmail>

    @POST("trips/{tripId}/participants/add")
    suspend fun addParticipant(
        @Path("tripId") tripId: String,
        @Body addParticipant: Participants
    ): Response<ResponseParticipants>

    @POST("/trips/{tripId}/participants/{participantId}/preferences")
    suspend fun addParticipantPreferences(
        @Path("tripId") tripId: String,
        @Path("participantId") participantId: String,
        @Body participantPreferences: DataParticipantPreferences
    ): Response<ParticipantPreferences>

    @POST("recommendations/preferences/{tripId}")
    suspend fun postMostPreferences(
        @Path("tripId") tripId: String
    ): Response<PostMostPreferences>

    @POST("recommendations/trip/{tripId}/recommendations")
    suspend fun postRecommendations(
        @Path("tripId") tripId: String
    ): Response<PostRecommendations>

    @GET("recommendations/{tripId}")
    suspend fun getRecommendations(
        @Path("tripId") tripId: String
    ): Response<GetRecommendation>

    @GET("recommendations/{tripId}/{iteneraryId}")
    suspend fun getIteneraries(
        @Path("tripId") tripId: String,
        @Path("iteneraryId") iteneraryId: String
    ): Response<ResponseItenerary>

    @DELETE("trips/{tripId}/participants/{participantId}")
    suspend fun deleteParticipant(
        @Path("tripId") tripId: String,
        @Path("participantId") participantId: String
    ): Response<ResponseParticipants>

    @POST("trips/{tripId}/vote/{participantId}/{iteneraryId}")
    suspend fun voteItenerary(
        @Path("tripId") tripId: String,
        @Path("participantId") participantId: String,
        @Path("iteneraryId") iteneraryId: String
    ) : Response<ResponseVote>

    @GET("trips/{tripId}/users/{participantId}/check-vote")
    suspend fun checkUserAlreadyVoting(
        @Path("tripId") tripId: String,
        @Path("participantId") participantId: String
    ): Response<ResponseVote>

}