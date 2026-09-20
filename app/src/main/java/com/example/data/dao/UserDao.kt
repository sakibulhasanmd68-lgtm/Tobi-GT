package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getActiveUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getActiveUser(): UserEntity?

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET coins = coins + :delta WHERE userId = :userId")
    suspend fun addCoins(userId: String, delta: Long)

    @Query("UPDATE users SET coins = :newCoins WHERE userId = :userId")
    suspend fun setCoins(userId: String, newCoins: Long)

    @Query("UPDATE users SET highScore = :highScore WHERE userId = :userId AND :highScore > highScore")
    suspend fun updateHighScore(userId: String, highScore: Int)

    @Query("UPDATE users SET gamesPlayed = gamesPlayed + 1 WHERE userId = :userId")
    suspend fun incrementGamesPlayed(userId: String)

    @Query("UPDATE users SET enemiesDestroyed = enemiesDestroyed + :count WHERE userId = :userId")
    suspend fun incrementEnemies(userId: String, count: Int)

    @Query("UPDATE users SET bossesDefeated = bossesDefeated + :count WHERE userId = :userId")
    suspend fun incrementBosses(userId: String, count: Int)

    @Query("UPDATE users SET missionsCompleted = missionsCompleted + 1 WHERE userId = :userId")
    suspend fun incrementMissionsCompleted(userId: String)
}
