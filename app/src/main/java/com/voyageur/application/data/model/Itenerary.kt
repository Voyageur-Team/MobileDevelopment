package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseItenerary(

	@field:SerializedName("recommendations")
	val recommendations: DataItenerary,

	@field:SerializedName("tripId")
	val tripId: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItenerary(

	@field:SerializedName("idItenerary")
	val idItenerary: String,

	@field:SerializedName("itineraryName")
	val itineraryName: String,

	@field:SerializedName("itinerary")
	val itinerary: List<IteneraryItem>
)

data class IteneraryItem(

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String
)
