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

data class ParticipantsPreferences(
	@field:SerializedName("userName") val userName: String?,
	@field:SerializedName("userId") val userId: String?,
	@field:SerializedName("email") val email: String?,
	@field:SerializedName("available_dates") val availableDates: List<String>?,
	@field:SerializedName("budget_range") val budgetRange: List<Int>?,
	@field:SerializedName("preferred_category") val preferredCategory: List<String>?,
	@field:SerializedName("preferred_destinations") val preferredDestinations: List<String>?
)

data class Participants(
	@field:SerializedName("userName") val userName: String?,
	@field:SerializedName("userId") val userId: String?,
	@field:SerializedName("email") val email: String?,
)
