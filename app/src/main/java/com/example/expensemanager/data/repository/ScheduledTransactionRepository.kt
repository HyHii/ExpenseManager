package com.example.expensemanager.data.repository

import com.example.expensemanager.data.local.ScheduledTransactionDao
import com.example.expensemanager.data.local.ScheduledTransactionEntity
import kotlinx.coroutines.flow.Flow

class ScheduledTransactionRepository(
    private val dao: ScheduledTransactionDao
) {
    val allScheduled: Flow<List<ScheduledTransactionEntity>> =
        dao.getAllScheduled()

    val activeScheduled: Flow<List<ScheduledTransactionEntity>> =
        dao.getActiveScheduled()

    suspend fun insert(scheduledTransaction: ScheduledTransactionEntity) {
        dao.insert(scheduledTransaction)
    }

    suspend fun update(scheduledTransaction: ScheduledTransactionEntity) {
        dao.update(scheduledTransaction)
    }

    suspend fun delete(scheduledTransaction: ScheduledTransactionEntity) {
        dao.delete(scheduledTransaction)
    }
}