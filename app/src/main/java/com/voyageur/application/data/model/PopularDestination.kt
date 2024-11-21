package com.voyageur.application.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularDestination(
    val name: String,
    val image: Int,
    val rating: Float,
    val location: String,
    val description: String,
    val price: Float
) : Parcelable