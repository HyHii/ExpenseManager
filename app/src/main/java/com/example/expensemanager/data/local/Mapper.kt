package com.example.expensemanager.data.local

import com.example.expensemanager.model.Transaction

fun TransactionEntity.toModel(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
        note = note,
        type = type
    )
}