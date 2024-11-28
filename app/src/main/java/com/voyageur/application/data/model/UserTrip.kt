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
	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createdBy")
	val createdBy: String,

	@field:SerializedName("inviteLink")
	val inviteLink: Any,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("votes")
	val votes: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("participants")
	val participants: List<Participants>
)