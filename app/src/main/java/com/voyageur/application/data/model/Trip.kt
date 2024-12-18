package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class CreateTrip(
	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("participants")
	val participants: List<Participants>
)

data class ResponseTrip(
	@field:SerializedName("data")
	val data: DataTrip,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ResponseTripUser(
	@field:SerializedName("data")
	val data: DataTripUser,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataTrip(
	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createdBy")
	val createdBy: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("votes")
	val votes: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("participants")
	val participants: List<Participants>,

	@field:SerializedName("average_budget_range")
	val averageBudgetRange: String? = null,

	@field:SerializedName("most_common_categories")
	val mostCommonCategories: List<String>? = null,

	@field:SerializedName("trip_end_date")
	val tripEndDate: String? = null,

	@field:SerializedName("trip_start_date")
	val tripStartDate: String? = null,

	@field:SerializedName("most_common_destination")
	val mostCommonDestination: String? = null
)

data class DataTripUser(
	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createdBy")
	val createdBy: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("votes")
	val votes: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("participants")
	val participants: ParticipantsPreferences,

	@field:SerializedName("average_budget_range")
	val averageBudgetRange: String? = null,

	@field:SerializedName("most_common_categories")
	val mostCommonCategories: List<String>? = null,

	@field:SerializedName("trip_end_date")
	val tripEndDate: String? = null,

	@field:SerializedName("trip_start_date")
	val tripStartDate: String? = null,

	@field:SerializedName("most_common_destination")
	val mostCommonDestination: String? = null
)


