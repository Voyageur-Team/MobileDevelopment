package com.voyageur.application.view.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Formatted {
    companion object {
        fun formatRupiah(amount: Double): String {
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val formattedAmount = numberFormat.format(amount)
            return formattedAmount.replace("Rp", "Rp. ").replace(",00", "")
        }

        fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun formatRating(rating: Double): String {
            return String.format("%.1f", rating / 10)
        }

        fun checkTripStatus(startDate: String, endDate: String): String {
            val currentDate = Calendar.getInstance().time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)

            return when {
                currentDate.before(start) -> "Upcoming"
                currentDate.after(end) -> "History"
                else -> "Ongoing"
            }
        }

    }
}