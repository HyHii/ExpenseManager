package com.example.expensemanager.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale
import kotlin.math.abs

object CurrencyCode {
    const val VND = "VND"
    const val USD = "USD"
    const val EUR = "EUR"
    const val JPY = "JPY"

    val all = listOf(
        VND,
        USD,
        EUR,
        JPY
    )

    fun symbolOf(currencyCode: String): String {
        return when (currencyCode) {
            USD -> "\$"
            EUR -> "€"
            JPY -> "¥"
            else -> ""
        }
    }

    fun labelOf(currencyCode: String): String {
        return when (currencyCode) {
            VND -> "Vietnamese Dong (VND)"
            USD -> "US Dollar (USD)"
            EUR -> "Euro (EUR)"
            JPY -> "Japanese Yen (JPY)"
            else -> currencyCode
        }
    }
}

object CurrencyDisplayConfig {
    var selectedCurrencyCode by mutableStateOf(CurrencyCode.VND)
        private set

    var exchangeRate by mutableStateOf(1.0)
        private set

    fun update(
        currencyCode: String,
        rate: Double
    ) {
        selectedCurrencyCode = currencyCode
        exchangeRate = if (currencyCode == CurrencyCode.VND) {
            1.0
        } else {
            rate.coerceAtLeast(0.000001)
        }
    }
}

fun convertFromVnd(
    amountInVnd: Double
): Double {
    return if (CurrencyDisplayConfig.selectedCurrencyCode == CurrencyCode.VND) {
        amountInVnd
    } else {
        amountInVnd / CurrencyDisplayConfig.exchangeRate
    }
}

fun formatCurrency(
    amountInVnd: Double
): String {
    val currencyCode = CurrencyDisplayConfig.selectedCurrencyCode
    val convertedAmount = convertFromVnd(amountInVnd)

    return when (currencyCode) {
        CurrencyCode.VND -> {
            "%,.0f VND".format(Locale.US, convertedAmount)
        }

        CurrencyCode.JPY -> {
            "${CurrencyCode.symbolOf(currencyCode)}%,.0f".format(Locale.US, convertedAmount)
        }

        CurrencyCode.USD,
        CurrencyCode.EUR -> {
            "${CurrencyCode.symbolOf(currencyCode)}%,.2f".format(Locale.US, convertedAmount)
        }

        else -> {
            "%,.0f VND".format(Locale.US, amountInVnd)
        }
    }
}

fun formatCompactCurrency(
    amountInVnd: Double
): String {
    val currencyCode = CurrencyDisplayConfig.selectedCurrencyCode
    val convertedAmount = convertFromVnd(amountInVnd)
    val absoluteAmount = abs(convertedAmount)

    val compactText = when {
        absoluteAmount >= 1_000_000_000 -> {
            formatOneDecimal(absoluteAmount / 1_000_000_000) + "B"
        }

        absoluteAmount >= 1_000_000 -> {
            formatOneDecimal(absoluteAmount / 1_000_000) + "M"
        }

        absoluteAmount >= 1_000 -> {
            formatOneDecimal(absoluteAmount / 1_000) + "K"
        }

        else -> {
            if (currencyCode == CurrencyCode.VND || currencyCode == CurrencyCode.JPY) {
                absoluteAmount.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", absoluteAmount)
            }
        }
    }

    val signedText = if (convertedAmount < 0) {
        "-$compactText"
    } else {
        compactText
    }

    return when (currencyCode) {
        CurrencyCode.VND -> "$signedText VND"
        CurrencyCode.USD -> "${CurrencyCode.symbolOf(currencyCode)}$signedText"
        CurrencyCode.EUR -> "${CurrencyCode.symbolOf(currencyCode)}$signedText"
        CurrencyCode.JPY -> "${CurrencyCode.symbolOf(currencyCode)}$signedText"
        else -> "$signedText VND"
    }
}

fun formatCompactNumber(
    amountInVnd: Double
): String {
    val currencyCode = CurrencyDisplayConfig.selectedCurrencyCode
    val convertedAmount = convertFromVnd(amountInVnd)
    val absoluteAmount = abs(convertedAmount)

    val compactText = when {
        absoluteAmount >= 1_000_000_000 -> {
            formatOneDecimal(absoluteAmount / 1_000_000_000) + "B"
        }

        absoluteAmount >= 1_000_000 -> {
            formatOneDecimal(absoluteAmount / 1_000_000) + "M"
        }

        absoluteAmount >= 1_000 -> {
            formatOneDecimal(absoluteAmount / 1_000) + "K"
        }

        else -> {
            if (currencyCode == CurrencyCode.VND || currencyCode == CurrencyCode.JPY) {
                absoluteAmount.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", absoluteAmount)
            }
        }
    }

    return if (convertedAmount < 0) {
        "-$compactText"
    } else {
        compactText
    }
}

private fun formatOneDecimal(
    value: Double
): String {
    val text = String.format(Locale.US, "%.1f", value)
    return text.removeSuffix(".0")
}