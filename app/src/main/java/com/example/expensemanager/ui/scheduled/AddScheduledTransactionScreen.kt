package com.example.expensemanager.ui.scheduled

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
import com.example.expensemanager.ui.components.CategoryGroupDropdown
import com.example.expensemanager.utils.ScheduledFrequency
import com.example.expensemanager.utils.scheduledCategoryGroups
import com.example.expensemanager.viewmodel.ScheduledTransactionViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import com.example.expensemanager.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduledTransactionScreen(
    navController: NavController,
    scheduledTransactionViewModel: ScheduledTransactionViewModel
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedDueDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var reminderDaysText by remember { mutableStateOf("3") }

    var selectedFrequency by remember {
        mutableStateOf(ScheduledFrequency.MONTHLY)
    }

    var frequencyExpanded by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Scheduled",
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
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Title")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Amount")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            CategoryGroupDropdown(
                categoryGroups = scheduledCategoryGroups,
                selectedGroup = selectedGroup,
                selectedSubCategory = category,
                onGroupSelected = { group ->
                    selectedGroup = group
                    category = ""
                },
                onSubCategorySelected = { subCategory ->
                    category = subCategory
                }
            )

            ExposedDropdownMenuBox(
                expanded = frequencyExpanded,
                onExpandedChange = {
                    frequencyExpanded = !frequencyExpanded
                }
            ) {
                OutlinedTextField(
                    value = ScheduledFrequency.labelOf(selectedFrequency),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = {
                        Text("Frequency")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = frequencyExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = frequencyExpanded,
                    onDismissRequest = {
                        frequencyExpanded = false
                    }
                ) {
                    ScheduledFrequency.all.forEach { frequency ->
                        DropdownMenuItem(
                            text = {
                                Text(ScheduledFrequency.labelOf(frequency))
                            },
                            onClick = {
                                selectedFrequency = frequency
                                frequencyExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = selectedDueDate?.let { formatDate(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker = true
                    },
                label = {
                    Text("Next due date")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                singleLine = true
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                selectedDueDate = datePickerState.selectedDateMillis
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }

            OutlinedTextField(
                value = reminderDaysText,
                onValueChange = {
                    reminderDaysText = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Reminder days before")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = note,
                onValueChange = {
                    note = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Note")
                }
            )

            errorMessage?.let { message ->
                Text(text = message)
            }

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    val nextDueDate = selectedDueDate
                    val reminderDays = reminderDaysText.toIntOrNull()

                    when {
                        title.isBlank() -> {
                            errorMessage = "Title cannot be empty."
                        }

                        amount == null || amount <= 0 -> {
                            errorMessage = "Amount must be greater than 0."
                        }

                        category.isBlank() -> {
                            errorMessage = "Category cannot be empty."
                        }

                        nextDueDate == null -> {
                            errorMessage = "Please select next due date."
                        }

                        reminderDays == null || reminderDays < 0 -> {
                            errorMessage = "Reminder days must be 0 or greater."
                        }

                        else -> {
                            scheduledTransactionViewModel.addScheduledTransaction(
                                title = title.trim(),
                                amount = amount,
                                category = category.trim(),
                                note = note.trim(),
                                type = "expense",
                                frequency = selectedFrequency,
                                nextDueDate = nextDueDate,
                                reminderDaysBefore = reminderDays
                            )

                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Scheduled Transaction")
            }
        }
    }
}