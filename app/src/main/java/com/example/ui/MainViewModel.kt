package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.billing.BillingService
import com.example.billing.PurchaseResult
import com.example.data.TobiGtDatabase
import com.example.data.model.AdminAuditLogEntity
import com.example.data.model.CoinPackage
import com.example.data.model.MissionEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import com.example.data.repository.AdminSummaryStats
import com.example.data.repository.LeaderboardEntry
import com.example.data.repository.TobiGtRepository
import com.example.game.GameAudio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TobiGtDatabase.getDatabase(application)
    val repository = TobiGtRepository(database)
    val billingService = BillingService(repository)
    val gameAudio = GameAudio()

    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val activeUser: StateFlow<UserEntity?> = repository.activeUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMissions: StateFlow<List<MissionEntity>> = repository.allMissionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminLogs: StateFlow<List<AdminAuditLogEntity>> = repository.adminLogsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _adminSummary = MutableStateFlow<AdminSummaryStats?>(null)
    val adminSummary: StateFlow<AdminSummaryStats?> = _adminSummary.asStateFlow()

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    private val _isTestPaymentMode = MutableStateFlow(true) // Defaults to developer test payment support
    val isTestPaymentMode: StateFlow<Boolean> = _isTestPaymentMode.asStateFlow()

    private val _isPurchasing = MutableStateFlow(false)
    val isPurchasing: StateFlow<Boolean> = _isPurchasing.asStateFlow()

    private val _purchaseFeedback = MutableStateFlow<String?>(null)
    val purchaseFeedback: StateFlow<String?> = _purchaseFeedback.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
            refreshLeaderboard()
            refreshAdminSummary()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
        if (screen == AppScreen.LEADERBOARD) {
            refreshLeaderboard()
        } else if (screen == AppScreen.ADMIN_DASHBOARD) {
            refreshAdminSummary()
        }
    }

    fun toggleTestPaymentMode() {
        _isTestPaymentMode.value = !_isTestPaymentMode.value
    }

    fun toggleSound() {
        val newState = !_isSoundEnabled.value
        _isSoundEnabled.value = newState
        gameAudio.isSoundEnabled = newState
    }

    fun clearFeedback() {
        _purchaseFeedback.value = null
    }

    fun executePurchase(pkg: CoinPackage) {
        if (_isPurchasing.value) return
        _isPurchasing.value = true
        _purchaseFeedback.value = "Contacting payment service..."

        viewModelScope.launch {
            val result = if (_isTestPaymentMode.value) {
                billingService.executeTestPurchase(pkg)
            } else {
                billingService.executePlayBillingPurchase(pkg)
            }

            when (result) {
                is PurchaseResult.Success -> {
                    gameAudio.playPowerUpSound()
                    _purchaseFeedback.value = "SUCCESS: Credited ${result.coinsCredited} Coins! Order #${result.transaction.orderId}"
                }
                is PurchaseResult.Error -> {
                    _purchaseFeedback.value = "FAILED: ${result.message}"
                }
                PurchaseResult.Cancelled -> {
                    _purchaseFeedback.value = "Purchase cancelled."
                }
            }
            _isPurchasing.value = false
            refreshAdminSummary()
        }
    }

    fun claimMission(missionId: String) {
        viewModelScope.launch {
            val success = repository.claimMissionReward(missionId)
            if (success) {
                gameAudio.playCoinSound()
            }
        }
    }

    fun onGameFinished(score: Int, enemiesKilled: Int, bossesKilled: Int, coinsCollected: Int) {
        viewModelScope.launch {
            repository.recordGameFinished(score, enemiesKilled, bossesKilled, coinsCollected)
            refreshLeaderboard()
        }
    }

    fun adminAdjustCoins(userId: String, newCoins: Long, reason: String) {
        viewModelScope.launch {
            repository.adminAdjustCoins(userId, newCoins, reason)
            refreshAdminSummary()
        }
    }

    fun adminRefund(transactionId: Long, reason: String) {
        viewModelScope.launch {
            repository.adminRefundTransaction(transactionId, reason)
            refreshAdminSummary()
        }
    }

    fun refreshLeaderboard() {
        viewModelScope.launch {
            _leaderboard.value = repository.getLeaderboard()
        }
    }

    fun refreshAdminSummary() {
        viewModelScope.launch {
            _adminSummary.value = repository.getAdminSummary()
        }
    }
}
