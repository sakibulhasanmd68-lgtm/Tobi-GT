package com.example.data.repository

import com.example.data.TobiGtDatabase
import com.example.data.model.AdminAuditLogEntity
import com.example.data.model.MissionEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random

data class LeaderboardEntry(
    val rank: Int,
    val displayName: String,
    val score: Int,
    val isCurrentUser: Boolean = false
)

data class AdminSummaryStats(
    val totalUsers: Int,
    val totalCoinsCirculation: Long,
    val totalTransactions: Int,
    val testTransactions: Int,
    val successfulTransactions: Int,
    val failedTransactions: Int,
    val refundedTransactions: Int,
    val totalRealRevenue: Double
)

class TobiGtRepository(private val database: TobiGtDatabase) {
    private val userDao = database.userDao()
    private val transactionDao = database.transactionDao()
    private val missionDao = database.missionDao()
    private val adminDao = database.adminAuditLogDao()

    val activeUserFlow: Flow<UserEntity?> = userDao.getActiveUserFlow()
    val allTransactionsFlow: Flow<List<TransactionEntity>> = transactionDao.getAllTransactionsFlow()
    val allMissionsFlow: Flow<List<MissionEntity>> = missionDao.getAllMissionsFlow()
    val adminLogsFlow: Flow<List<AdminAuditLogEntity>> = adminDao.getAllLogsFlow()

    suspend fun ensureInitialized(): UserEntity = withContext(Dispatchers.IO) {
        var user = userDao.getActiveUser()
        if (user == null) {
            val randomNum = 1000 + Random.nextInt(9000)
            val newUserId = "anon_" + UUID.randomUUID().toString().replace("-", "").take(16)
            user = UserEntity(
                userId = newUserId,
                displayName = "Pilot #$randomNum",
                coins = 250L,
                highScore = 0,
                gamesPlayed = 0,
                enemiesDestroyed = 0,
                bossesDefeated = 0,
                missionsCompleted = 0
            )
            userDao.insertUser(user)
        }

        // Initialize missions if none
        val existingMissions = missionDao.getAllMissions()
        if (existingMissions.isEmpty()) {
            val defaultMissions = listOf(
                MissionEntity(
                    id = "m_play_1",
                    title = "First Sortie",
                    description = "Deploy your fighter aircraft for 1 sky battle.",
                    targetCount = 1,
                    rewardCoins = 100L,
                    missionType = "GAMES_PLAYED"
                ),
                MissionEntity(
                    id = "m_destroy_10",
                    title = "Target Interceptor",
                    description = "Shoot down 10 hostile enemy aircraft.",
                    targetCount = 10,
                    rewardCoins = 150L,
                    missionType = "DESTROY_ENEMIES"
                ),
                MissionEntity(
                    id = "m_collect_100",
                    title = "Coin Harvester",
                    description = "Collect 100 golden energy coins during flight.",
                    targetCount = 100,
                    rewardCoins = 200L,
                    missionType = "COLLECT_COINS"
                ),
                MissionEntity(
                    id = "m_score_2000",
                    title = "Ace Marksman",
                    description = "Reach a high score of 2,000 points in a single battle.",
                    targetCount = 2000,
                    rewardCoins = 300L,
                    missionType = "REACH_SCORE"
                ),
                MissionEntity(
                    id = "m_defeat_boss",
                    title = "Titan Vanquisher",
                    description = "Defeat the Titan Dreadnought sky fortress boss.",
                    targetCount = 1,
                    rewardCoins = 500L,
                    missionType = "DEFEAT_BOSS"
                ),
                MissionEntity(
                    id = "m_veteran_pilot",
                    title = "Apex Squadron",
                    description = "Destroy 50 enemy combat units.",
                    targetCount = 50,
                    rewardCoins = 750L,
                    missionType = "DESTROY_ENEMIES"
                )
            )
            missionDao.insertMissions(defaultMissions)
        }

        user
    }

    suspend fun getActiveUser(): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getActiveUser()
    }

    suspend fun recordGameFinished(
        score: Int,
        enemiesKilled: Int,
        bossesKilled: Int,
        coinsCollected: Int
    ): Long = withContext(Dispatchers.IO) {
        val user = userDao.getActiveUser() ?: ensureInitialized()
        val userId = user.userId

        userDao.incrementGamesPlayed(userId)
        userDao.incrementEnemies(userId, enemiesKilled)
        userDao.incrementBosses(userId, bossesKilled)
        userDao.updateHighScore(userId, score)
        userDao.addCoins(userId, coinsCollected.toLong())

        // Update missions progress
        val missions = missionDao.getAllMissions()
        val updatedUser = userDao.getActiveUser() ?: user
        for (m in missions) {
            if (!m.isClaimed) {
                var progress = m.currentProgress
                when (m.missionType) {
                    "GAMES_PLAYED" -> progress = updatedUser.gamesPlayed
                    "DESTROY_ENEMIES" -> progress = updatedUser.enemiesDestroyed
                    "COLLECT_COINS" -> progress = (progress + coinsCollected).coerceAtMost(m.targetCount)
                    "REACH_SCORE" -> progress = maxOf(progress, score)
                    "DEFEAT_BOSS" -> progress = updatedUser.bossesDefeated
                }
                if (progress != m.currentProgress) {
                    missionDao.updateProgress(m.id, progress)
                }
            }
        }

        userDao.getActiveUser()?.coins ?: 0L
    }

    suspend fun claimMissionReward(missionId: String): Boolean = withContext(Dispatchers.IO) {
        val mission = missionDao.getMissionById(missionId) ?: return@withContext false
        if (mission.isCompleted && !mission.isClaimed) {
            val user = userDao.getActiveUser() ?: return@withContext false
            missionDao.markClaimed(missionId)
            userDao.addCoins(user.userId, mission.rewardCoins)
            userDao.incrementMissionsCompleted(user.userId)
            return@withContext true
        }
        false
    }

    suspend fun isPurchaseTokenDuplicate(token: String): Boolean = withContext(Dispatchers.IO) {
        transactionDao.getTransactionByToken(token) != null
    }

    suspend fun recordAndCreditPurchase(
        productId: String,
        packageName: String,
        price: Double,
        currency: String,
        coinAmount: Long,
        paymentProvider: String,
        purchaseToken: String,
        orderId: String
    ): Result<TransactionEntity> = withContext(Dispatchers.IO) {
        // Strict Idempotency / Duplicate Check
        if (transactionDao.getTransactionByToken(purchaseToken) != null) {
            return@withContext Result.failure(IllegalStateException("Duplicate purchase token detected: Transaction already processed."))
        }

        val user = userDao.getActiveUser() ?: ensureInitialized()
        val now = System.currentTimeMillis()

        val transaction = TransactionEntity(
            anonymousUserId = user.userId,
            productId = productId,
            packageName = packageName,
            price = price,
            currency = currency,
            coinAmount = coinAmount,
            paymentProvider = paymentProvider,
            purchaseToken = purchaseToken,
            orderId = orderId,
            paymentStatus = "SUCCESS",
            verificationStatus = "VERIFIED",
            createdTimestamp = now,
            verifiedTimestamp = now,
            creditedTimestamp = now
        )

        try {
            transactionDao.insertTransaction(transaction)
            // Credit authoritative backend coin balance
            userDao.addCoins(user.userId, coinAmount)
            Result.success(transaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun adminAdjustCoins(userId: String, newCoins: Long, reason: String): Boolean = withContext(Dispatchers.IO) {
        val user = userDao.getUserById(userId) ?: return@withContext false
        val oldCoins = user.coins
        userDao.setCoins(userId, newCoins)
        adminDao.insertLog(
            AdminAuditLogEntity(
                adminAction = "COIN_ADJUSTMENT",
                targetUserId = userId,
                previousValue = "$oldCoins Coins",
                newValue = "$newCoins Coins",
                reason = reason
            )
        )
        true
    }

    suspend fun adminRefundTransaction(transactionId: Long, reason: String): Boolean = withContext(Dispatchers.IO) {
        val transactions = transactionDao.getAllTransactions()
        val tx = transactions.find { it.id == transactionId } ?: return@withContext false
        if (tx.paymentStatus == "REFUNDED") return@withContext false

        val updated = tx.copy(paymentStatus = "REFUNDED")
        transactionDao.updateTransaction(updated)
        // Deduct credited coins if refundable
        userDao.addCoins(tx.anonymousUserId, -tx.coinAmount)
        adminDao.insertLog(
            AdminAuditLogEntity(
                adminAction = "PURCHASE_REFUND",
                targetUserId = tx.anonymousUserId,
                previousValue = "SUCCESS (${tx.coinAmount} coins)",
                newValue = "REFUNDED (-${tx.coinAmount} coins)",
                reason = reason
            )
        )
        true
    }

    suspend fun getAdminSummary(): AdminSummaryStats = withContext(Dispatchers.IO) {
        val users = userDao.getAllUsers()
        val totalUsers = users.size
        val totalCoins = users.sumOf { it.coins }
        val allTx = transactionDao.getAllTransactions()
        val successful = allTx.count { it.paymentStatus == "SUCCESS" }
        val testTx = allTx.count { it.paymentProvider == "TEST_DEVELOPMENT" }
        val refunded = allTx.count { it.paymentStatus == "REFUNDED" }
        val failed = allTx.count { it.paymentStatus == "FAILED" }
        val realRevenue = allTx.filter { it.paymentStatus == "SUCCESS" && it.paymentProvider != "TEST_DEVELOPMENT" }.sumOf { it.price }

        AdminSummaryStats(
            totalUsers = totalUsers,
            totalCoinsCirculation = totalCoins,
            totalTransactions = allTx.size,
            testTransactions = testTx,
            successfulTransactions = successful,
            failedTransactions = failed,
            refundedTransactions = refunded,
            totalRealRevenue = realRevenue
        )
    }

    suspend fun getLeaderboard(): List<LeaderboardEntry> = withContext(Dispatchers.IO) {
        val user = userDao.getActiveUser() ?: ensureInitialized()
        val basePilots = listOf(
            Triple("Apex_Zero #9901", 145200, false),
            Triple("Valkyrie_GT #8820", 112450, false),
            Triple("Sky_Marauder #3314", 94100, false),
            Triple("Nova_Drift #1004", 76300, false),
            Triple("Spectre_Ace #5541", 58900, false),
            Triple("Phantom_Wing #6199", 42150, false),
            Triple("Orbital_Hawk #2045", 31800, false),
            Triple("Iron_Tempest #7782", 21500, false),
            Triple("Cyber_Raptor #4411", 14200, false),
            Triple("Vortex_Raven #9011", 8500, false)
        ).toMutableList()

        // Insert current user score
        val userScore = user.highScore
        val allPilots = (basePilots + Triple(user.displayName, userScore, true))
            .sortedByDescending { it.second }
            .take(15)

        allPilots.mapIndexed { index, triple ->
            LeaderboardEntry(
                rank = index + 1,
                displayName = triple.first,
                score = triple.second,
                isCurrentUser = triple.third
            )
        }
    }
}
