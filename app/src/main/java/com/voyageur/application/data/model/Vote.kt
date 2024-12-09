package com.voyageur.application.data.model

data class ResponseVote(
	val data: DataVote,
	val error: Boolean,
	val message: String
)

data class DataVote(
	val idItenerary: String,
	val tripId: String,
	val userId: String
)

