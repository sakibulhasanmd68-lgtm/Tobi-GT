package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["purchaseToken"], unique = true),
        Index(value = ["orderId"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val anonymousUserId: String,
    val productId: String,
    val packageName: String,
    val price: Double,
    val currency: String = "USD",
    val coinAmount: Long,
    val paymentProvider: String, // "GOOGLE_PLAY_BILLING" or "TEST_DEVELOPMENT"
    val purchaseToken: String, // Unique token used for idempotency / duplicate check
    val orderId: String,
    val paymentStatus: String, // "SUCCESS", "FAILED", "REFUNDED", "PENDING"
    val verificationStatus: String, // "VERIFIED", "UNVERIFIED"
    val createdTimestamp: Long = System.currentTimeMillis(),
    val verifiedTimestamp: Long = System.currentTimeMillis(),
    val creditedTimestamp: Long = System.currentTimeMillis()
) {
    val diamondAmount: Long
        get() = coinAmount
}
