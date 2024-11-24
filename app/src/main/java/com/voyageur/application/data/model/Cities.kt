package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseCities(

	@field:SerializedName("data")
	val data: List<Cities>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Cities(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
