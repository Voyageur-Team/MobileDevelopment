package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseParticipants(

	@field:SerializedName("data")
	val data: List<Participants>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Participants(
	@field:SerializedName("userName") val userName: String?,
	@field:SerializedName("userId") val userId: String?,
	@field:SerializedName("email") val email: String?
)

