package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class MissionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val targetCount: Int,
    val currentProgress: Int = 0,
    val rewardCoins: Long,
    val isClaimed: Boolean = false,
    val missionType: String // "GAMES_PLAYED", "DESTROY_ENEMIES", "COLLECT_COINS", "REACH_SCORE", "DEFEAT_BOSS"
) {
    val isCompleted: Boolean
        get() = currentProgress >= targetCount
}
