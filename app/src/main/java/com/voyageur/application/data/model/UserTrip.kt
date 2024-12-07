package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class UserTrip(
	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItem(
	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("trip_end_date")
	val tripEndDate: String,

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("average_budget_range")
	val averageBudgetRange: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createdBy")
	val createdBy: String,

	@field:SerializedName("most_common_destination")
	val mostCommonDestination: String,

	@field:SerializedName("most_common_categories")
	val mostCommonCategories: List<String>,

	@field:SerializedName("votes")
	val votes: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("trip_start_date")
	val tripStartDate: String,

	@field:SerializedName("most_available_dates")
	val mostAvailableDates: List<String>,

	@field:SerializedName("participants")
	val participants: List<Participants>
)