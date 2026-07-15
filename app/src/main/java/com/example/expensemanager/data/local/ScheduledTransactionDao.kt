package com.example.expensemanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledTransactionDao {

    @Query("SELECT * FROM scheduled_transactions ORDER BY nextDueDate ASC")
    fun getAllScheduled(): Flow<List<ScheduledTransactionEntity>>

    @Query("SELECT * FROM scheduled_transactions WHERE isActive = 1 ORDER BY nextDueDate ASC")
    fun getActiveScheduled(): Flow<List<ScheduledTransactionEntity>>

    @Insert
    suspend fun insert(scheduledTransaction: ScheduledTransactionEntity)

    @Update
    suspend fun update(scheduledTransaction: ScheduledTransactionEntity)

    @Delete
    suspend fun delete(scheduledTransaction: ScheduledTransactionEntity)
}