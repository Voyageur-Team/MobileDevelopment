package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

//PostMostPreferences
data class PostMostPreferences(

	@field:SerializedName("data")
	val data: DataMostRecommendation,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataMostRecommendation(

	@field:SerializedName("tripStartDate")
	val tripStartDate: String,

	@field:SerializedName("mostCommonDates")
	val mostCommonDates: List<String>,

	@field:SerializedName("tripEndDate")
	val tripEndDate: String,

	@field:SerializedName("commonDestination")
	val commonDestination: String,

	@field:SerializedName("commonCategories")
	val commonCategories: List<String>,

	@field:SerializedName("intermediateBudget")
	val intermediateBudget: Int
)

//Hasil Rekomendasi
data class PostRecommendations(

	@field:SerializedName("idRecommendation")
	val idRecommendation: String,

	@field:SerializedName("data")
	val data: List<Iteneraries>,

	@field:SerializedName("tripId")
	val tripId: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Iteneraries(

	@field:SerializedName("idItenerary")
	val itineraryId: String,

	@field:SerializedName("itineraryName")
	val itineraryName: String,

	@field:SerializedName("itinerary")
	val itinerary: List<ItineraryItem>
)

data class ItineraryItem(

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

//Get Rekomendasi
data class GetRecommendation(

	@field:SerializedName("tripId")
	val tripId: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("recommendations")
	val recommendations: List<Iteneraries>
)



