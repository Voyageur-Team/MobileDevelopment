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

data class UserPreffered(

    @field:SerializedName("preferred_category")
    val preferredCategory: List<String>,

    @field:SerializedName("budget_range")
    val budgetRange: List<Int>,

    @field:SerializedName("preferred_destinations")
    val preferredDestinations: List<String>,

    @field:SerializedName("userName")
    val userName: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("available_dates")
    val availableDates: List<String>
)

data class UserPrefferedResponse(

    @field:SerializedName("data")
    val data: UserPreffered,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
