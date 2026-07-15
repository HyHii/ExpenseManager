package com.example.expensemanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.data.local.ScheduledTransactionEntity
import com.example.expensemanager.data.repository.ScheduledTransactionRepository
import com.example.expensemanager.utils.calculateNextDueDate
import com.example.expensemanager.utils.isReminderVisible
import kotlinx.coroutines.launch

class ScheduledTransactionViewModel(
    private val repository: ScheduledTransactionRepository
) : ViewModel() {

    private var scheduledCache by mutableStateOf<List<ScheduledTransactionEntity>>(emptyList())

    init {
        viewModelScope.launch {
            repository.allScheduled.collect { scheduledTransactions ->
                scheduledCache = scheduledTransactions
            }
        }
    }

    fun allScheduled(): List<ScheduledTransactionEntity> {
        return scheduledCache.sortedBy { it.nextDueDate }
    }

    fun activeScheduled(): List<ScheduledTransactionEntity> {
        return scheduledCache
            .filter { it.isActive }
            .sortedBy { it.nextDueDate }
    }

    fun upcomingReminders(): List<ScheduledTransactionEntity> {
        return scheduledCache
            .filter { it.isActive }
            .filter { scheduledTransaction ->
                isReminderVisible(
                    nextDueDate = scheduledTransaction.nextDueDate,
                    reminderDaysBefore = scheduledTransaction.reminderDaysBefore
                )
            }
            .sortedBy { it.nextDueDate }
    }

    fun addScheduledTransaction(
        title: String,
        amount: Double,
        category: String,
        note: String,
        type: String,
        frequency: String,
        nextDueDate: Long,
        reminderDaysBefore: Int
    ) {
        viewModelScope.launch {
            repository.insert(
                ScheduledTransactionEntity(
                    title = title,
                    amount = amount,
                    category = category,
                    note = note,
                    type = type,
                    frequency = frequency,
                    nextDueDate = nextDueDate,
                    reminderDaysBefore = reminderDaysBefore,
                    isActive = true
                )
            )
        }
    }

    fun moveToNextCycle(scheduledTransaction: ScheduledTransactionEntity) {
        viewModelScope.launch {
            val nextDate = calculateNextDueDate(
                currentDueDate = scheduledTransaction.nextDueDate,
                frequency = scheduledTransaction.frequency
            )

            repository.update(
                scheduledTransaction.copy(
                    nextDueDate = nextDate
                )
            )
        }
    }

    fun toggleActive(scheduledTransaction: ScheduledTransactionEntity) {
        viewModelScope.launch {
            repository.update(
                scheduledTransaction.copy(
                    isActive = !scheduledTransaction.isActive
                )
            )
        }
    }

    fun deleteScheduledTransaction(scheduledTransaction: ScheduledTransactionEntity) {
        viewModelScope.launch {
            repository.delete(scheduledTransaction)
        }
    }
}