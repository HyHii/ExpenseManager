package com.example.expensemanager.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseDateToMillis(dateText: String): Long? {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        sdf.parse(dateText)?.time
    } catch (_: Exception) {
        null
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun formatCurrency(amount: Double): String {
    return "%,.0f VND".format(amount)
}