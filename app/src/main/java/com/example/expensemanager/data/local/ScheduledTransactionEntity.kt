package com.example.expensemanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_transactions")
data class ScheduledTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val amount: Double,
    val category: String,
    val note: String,

    val type: String,

    val frequency: String,

    val nextDueDate: Long,

    val reminderDaysBefore: Int,

    val isActive: Boolean = true
)