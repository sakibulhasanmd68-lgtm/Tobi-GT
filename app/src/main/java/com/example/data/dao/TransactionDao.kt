package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY createdTimestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY createdTimestamp DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE anonymousUserId = :userId ORDER BY createdTimestamp DESC")
    fun getTransactionsForUserFlow(userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE purchaseToken = :purchaseToken LIMIT 1")
    suspend fun getTransactionByToken(purchaseToken: String): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE orderId = :orderId LIMIT 1")
    suspend fun getTransactionByOrderId(orderId: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("SELECT COUNT(*) FROM transactions WHERE paymentStatus = 'SUCCESS'")
    suspend fun getSuccessfulCount(): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE paymentProvider = 'TEST_DEVELOPMENT'")
    suspend fun getTestTransactionsCount(): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE paymentStatus = 'REFUNDED'")
    suspend fun getRefundedCount(): Int

    @Query("SELECT COUNT(*) FROM transactions WHERE paymentStatus = 'FAILED'")
    suspend fun getFailedCount(): Int

    @Query("SELECT SUM(price) FROM transactions WHERE paymentStatus = 'SUCCESS' AND paymentProvider != 'TEST_DEVELOPMENT'")
    suspend fun getTotalRealRevenue(): Double?

    @Query("SELECT SUM(coinAmount) FROM transactions WHERE paymentStatus = 'SUCCESS'")
    suspend fun getTotalCoinsPurchased(): Long?
}
