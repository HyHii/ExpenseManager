package com.example.expensemanager.model

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val note: String,
    val type: String
)