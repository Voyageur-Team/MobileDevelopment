package com.voyageur.application.view.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
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
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun formatRating(rating: Double): String {
            return String.format("%.1f", rating / 10)
        }
    }
}