package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_audit_logs")
data class AdminAuditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val adminAction: String, // "COIN_ADJUSTMENT", "PURCHASE_REFUND", "SYSTEM_RESET"
    val targetUserId: String,
    val previousValue: String,
    val newValue: String,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
