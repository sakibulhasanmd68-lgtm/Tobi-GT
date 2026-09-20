package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val displayName: String,
    val coins: Long = 250L, // Starter bonus diamonds
    val highScore: Int = 0,
    val gamesPlayed: Int = 0,
    val enemiesDestroyed: Int = 0,
    val bossesDefeated: Int = 0,
    val missionsCompleted: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    val diamonds: Long
        get() = coins
}
