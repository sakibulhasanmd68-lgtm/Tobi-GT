package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CoinPackage
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CoinStoreScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.PlayConsoleDocsScreen
import com.example.ui.screens.PlayPointsScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TobiGtApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TobiGtApp(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeUser by viewModel.activeUser.collectAsStateWithLifecycle()
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val allMissions by viewModel.allMissions.collectAsStateWithLifecycle()
    val adminLogs by viewModel.adminLogs.collectAsStateWithLifecycle()
    val adminSummary by viewModel.adminSummary.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val isTestPaymentMode by viewModel.isTestPaymentMode.collectAsStateWithLifecycle()
    val isPurchasing by viewModel.isPurchasing.collectAsStateWithLifecycle()
    val purchaseFeedback by viewModel.purchaseFeedback.collectAsStateWithLifecycle()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsStateWithLifecycle()

    val coins = activeUser?.coins ?: 0L
    val highScore = activeUser?.highScore ?: 0
    val pilotName = activeUser?.displayName ?: "Pilot #0000"
    val userId = activeUser?.userId ?: ""

    // Handle Back Press to return to HOME screen when deep in sub-screens
    if (currentScreen != AppScreen.HOME && currentScreen != AppScreen.SPLASH) {
        BackHandler {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.SPLASH -> {
                    SplashScreen(
                        onFinish = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.HOME -> {
                    HomeScreen(
                        coins = coins,
                        highScore = highScore,
                        pilotName = pilotName,
                        onNavigate = { screen -> viewModel.navigateTo(screen) }
                    )
                }

                AppScreen.GAME -> {
                    GameScreen(
                        gameAudio = viewModel.gameAudio,
                        highScore = highScore,
                        onGameFinished = { score, enemies, bosses, collectedCoins ->
                            viewModel.onGameFinished(score, enemies, bosses, collectedCoins)
                        },
                        onExitHome = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.COIN_STORE -> {
                    CoinStoreScreen(
                        coins = coins,
                        isTestPaymentMode = isTestPaymentMode,
                        isPurchasing = isPurchasing,
                        purchaseFeedback = purchaseFeedback,
                        onToggleTestPayment = { viewModel.toggleTestPaymentMode() },
                        onPurchasePackage = { pkg: CoinPackage -> viewModel.executePurchase(pkg) },
                        onClearFeedback = { viewModel.clearFeedback() },
                        onNavigatePlayPoints = { viewModel.navigateTo(AppScreen.PLAY_POINTS) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PLAY_POINTS -> {
                    PlayPointsScreen(
                        coins = coins,
                        onNavigatePlayConsoleDocs = { viewModel.navigateTo(AppScreen.PLAY_CONSOLE_DOCS) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.MISSIONS -> {
                    MissionsScreen(
                        coins = coins,
                        missions = allMissions,
                        onClaimMission = { id -> viewModel.claimMission(id) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.LEADERBOARD -> {
                    LeaderboardScreen(
                        coins = coins,
                        leaderboard = leaderboard,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PROFILE -> {
                    ProfileScreen(
                        user = activeUser,
                        transactions = allTransactions,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        coins = coins,
                        isSoundEnabled = isSoundEnabled,
                        isTestPaymentMode = isTestPaymentMode,
                        onToggleSound = { viewModel.toggleSound() },
                        onToggleTestPayment = { viewModel.toggleTestPaymentMode() },
                        onNavigateAdmin = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) },
                        onNavigatePlayConsoleDocs = { viewModel.navigateTo(AppScreen.PLAY_CONSOLE_DOCS) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        user = activeUser,
                        stats = adminSummary,
                        transactions = allTransactions,
                        auditLogs = adminLogs,
                        onAdjustCoins = { uId, newCoins, reason ->
                            viewModel.adminAdjustCoins(uId, newCoins, reason)
                        },
                        onRefundTransaction = { txId, reason ->
                            viewModel.adminRefund(txId, reason)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PLAY_CONSOLE_DOCS -> {
                    PlayConsoleDocsScreen(
                        coins = coins,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.PRIVACY_POLICY -> {
                    PrivacyPolicyScreen(
                        coins = coins,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.CONTACT -> {
                    ContactScreen(
                        coins = coins,
                        userId = userId,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}
