package com.example.expensemanager.data.repository

import com.example.expensemanager.data.local.TransactionDao
import com.example.expensemanager.data.local.TransactionEntity

class TransactionRepository(
    private val dao: TransactionDao
) {
    val all = dao.getAll()
    val expenses = dao.getExpenses()
    val income = dao.getIncome()

    suspend fun insert(t: TransactionEntity) = dao.insert(t)
    suspend fun delete(t: TransactionEntity) = dao.delete(t)
    suspend fun update(t: TransactionEntity) = dao.update(t)
    suspend fun getTransactionById(id: Int) = dao.getTransactionById(id)

}