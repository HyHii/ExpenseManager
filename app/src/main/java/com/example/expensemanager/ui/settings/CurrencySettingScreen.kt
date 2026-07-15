package com.example.expensemanager.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanager.utils.CurrencyCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySettingScreen(
    navController: NavController,
    currentCurrencyCode: String,
    currentExchangeRate: Double,
    onSaveCurrencySetting: (String, Double) -> Unit
) {
    var selectedCurrencyCode by remember {
        mutableStateOf(currentCurrencyCode)
    }

    var exchangeRateText by remember {
        mutableStateOf(
            if (currentCurrencyCode == CurrencyCode.VND) {
                "1"
            } else {
                currentExchangeRate.toString()
            }
        )
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val isVnd = selectedCurrencyCode == CurrencyCode.VND

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Currency Setting",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = CurrencyCode.labelOf(selectedCurrencyCode),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = {
                        Text("Display currency")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    CurrencyCode.all.forEach { currencyCode ->
                        DropdownMenuItem(
                            text = {
                                Text(CurrencyCode.labelOf(currencyCode))
                            },
                            onClick = {
                                selectedCurrencyCode = currencyCode

                                if (currencyCode == CurrencyCode.VND) {
                                    exchangeRateText = "1"
                                } else if (exchangeRateText == "1") {
                                    exchangeRateText = ""
                                }

                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = exchangeRateText,
                onValueChange = {
                    exchangeRateText = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = if (isVnd) {
                            "Exchange rate"
                        } else {
                            "VND for 1 $selectedCurrencyCode"
                        }
                    )
                },
                enabled = !isVnd,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    Text(
                        text = if (isVnd) {
                            "VND is the base currency, so exchange rate is fixed at 1."
                        } else {
                            "Example: if 1 $selectedCurrencyCode = 25,000 VND, enter 25000."
                        }
                    )
                }
            )

            errorMessage?.let { message ->
                Text(text = message)
            }

            Button(
                onClick = {
                    val rate = if (isVnd) {
                        1.0
                    } else {
                        exchangeRateText.toDoubleOrNull()
                    }

                    when {
                        selectedCurrencyCode.isBlank() -> {
                            errorMessage = "Please select a currency."
                        }

                        rate == null || rate <= 0.0 -> {
                            errorMessage = "Exchange rate must be greater than 0."
                        }

                        else -> {
                            onSaveCurrencySetting(
                                selectedCurrencyCode,
                                rate
                            )

                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Currency Setting")
            }
        }
    }
}