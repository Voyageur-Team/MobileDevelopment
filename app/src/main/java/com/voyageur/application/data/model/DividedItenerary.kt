package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseDividedItenerary(

	@field:SerializedName("data")
	val data: List<DividedItenerary>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DividedItenerary(

	@field:SerializedName("day")
	val day: Int,

	@field:SerializedName("items")
	val items: List<DividedIteneraryItems>
)

data class DividedIteneraryItems(

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
