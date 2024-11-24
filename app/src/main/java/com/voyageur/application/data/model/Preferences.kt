package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponsePreferences(

	@field:SerializedName("data")
	val data: List<Preferences>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Preferences(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
