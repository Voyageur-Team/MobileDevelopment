package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LoginResult(
	@field:SerializedName("userName")
	val userName: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("token")
	val token: String
)

data class LoginDataAccount(
	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String
)
