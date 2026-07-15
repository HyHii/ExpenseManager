package com.example.expensemanager.ui.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSavingGoalScreen(
    navController: NavController,
    currentGoalName: String,
    currentTargetAmount: Double,
    onSaveGoal: (String, Double) -> Unit,
    onClearGoal: () -> Unit
) {
    var goalNameText by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }

    var goalNameError by remember { mutableStateOf<String?>(null) }
    var targetError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentGoalName, currentTargetAmount) {
        goalNameText = currentGoalName
        targetText = if (currentTargetAmount > 0) {
            currentTargetAmount.toLong().toString()
        } else {
            ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Set Saving Goal",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                value = goalNameText,
                onValueChange = {
                    goalNameText = it
                    goalNameError = null
                },
                label = { Text("Goal name") },
                placeholder = { Text("Example: Buy laptop") },
                isError = goalNameError != null,
                supportingText = {
                    if (goalNameError != null) {
                        Text(goalNameError!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = targetText,
                onValueChange = {
                    targetText = it
                    targetError = null
                },
                label = { Text("Target amount") },
                isError = targetError != null,
                supportingText = {
                    if (targetError != null) {
                        Text(targetError!!)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val targetAmount = targetText.toDoubleOrNull()

                    goalNameError = when {
                        goalNameText.isBlank() -> "Goal name is required"
                        else -> null
                    }

                    targetError = when {
                        targetText.isBlank() -> "Target amount is required"
                        targetAmount == null -> "Invalid target amount"
                        targetAmount <= 0 -> "Target amount must be greater than 0"
                        else -> null
                    }

                    if (goalNameError == null && targetError == null) {
                        onSaveGoal(goalNameText.trim(), targetAmount!!)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Goal")
            }

            OutlinedButton(
                onClick = {
                    onClearGoal()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Goal")
            }
        }
    }
}