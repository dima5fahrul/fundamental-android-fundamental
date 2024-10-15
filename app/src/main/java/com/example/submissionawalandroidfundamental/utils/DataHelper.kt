package com.example.submissionawalandroidfundamental.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DataHelper {
    companion object {
        fun convertDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date!!)
        }

        fun getCurrentDate(): String {
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return outputFormat.format(System.currentTimeMillis())
        }
    }
}