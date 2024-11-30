package com.voyageur.application.data.model

import com.google.gson.annotations.SerializedName

data class ResponseUserEmail(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("users")
    val data: List<DataUserEmail>
)

data class DataUserEmail(
    @field:SerializedName("userId") val userId: String?,
    @field:SerializedName("userName") val userName: String?,
    @field:SerializedName("email") val email: String?
)

