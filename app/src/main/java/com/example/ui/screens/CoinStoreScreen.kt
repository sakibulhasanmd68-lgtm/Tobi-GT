package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoinPackage
import com.example.data.model.CoinPackages
import com.example.ui.components.CyberButton
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.LaserGreen
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary

@Composable
fun CoinStoreScreen(
    coins: Long,
    isTestPaymentMode: Boolean,
    isPurchasing: Boolean,
    purchaseFeedback: String?,
    onToggleTestPayment: () -> Unit,
    onPurchasePackage: (CoinPackage) -> Unit,
    onClearFeedback: () -> Unit,
    onNavigatePlayPoints: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("coin_store_screen")
    ) {
        TobiGtHeader(
            title = "COIN STORE",
            coins = coins,
            onBack = onBack
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Store Header: Payment Provider Toggle & Play Points Banner
            item(span = { GridItemSpan(2) }) {
                Column {
                    // Google Play Points Discount Callout Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable { onNavigatePlayPoints() }
                            .testTag("store_play_points_callout"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B).copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Loyalty,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Have Google Play Points?",
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Redeem coupons up to $40 OFF on Google Play",
                                    color = SpaceTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = "VIEW →",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mode Selection: Google Play Billing vs Test Development Provider
                    Surface(
                        color = SpaceCardBg,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SpaceCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = if (isTestPaymentMode) NeonAmber else CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isTestPaymentMode) "DEV TEST PAYMENT MODE" else "GOOGLE PLAY BILLING",
                                        color = if (isTestPaymentMode) NeonAmber else CyberCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = if (isTestPaymentMode) "Simulates full backend verification & ledger" else "Production Play Billing architecture",
                                    color = SpaceTextMuted,
                                    fontSize = 10.sp
                                )
                            }

                            Switch(
                                checked = isTestPaymentMode,
                                onCheckedChange = { onToggleTestPayment() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NeonAmber,
                                    checkedTrackColor = Color(0xFF78350F),
                                    uncheckedThumbColor = CyberCyan,
                                    uncheckedTrackColor = Color(0xFF0C4A6E)
                                ),
                                modifier = Modifier.testTag("payment_mode_switch")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Formula verification notice
                    Text(
                        text = "EXACT AUTHORITATIVE FORMULA: COINS = DOLLARS × 100 + 1",
                        color = SpaceTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // The 19 Packages
            items(CoinPackages.ALL) { pkg ->
                CoinPackageCard(
                    pkg = pkg,
                    isTestPaymentMode = isTestPaymentMode,
                    isPurchasing = isPurchasing,
                    onClick = { onPurchasePackage(pkg) }
                )
            }
        }

        // Purchase Feedback Modal / Banner
        AnimatedVisibility(
            visible = purchaseFeedback != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                color = SpaceCardBg,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                border = BorderStroke(1.dp, CyberCyan),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("purchase_feedback_panel")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isPurchasing) {
                        CircularProgressIndicator(
                            color = CyberCyan,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (purchaseFeedback?.contains("SUCCESS") == true) LaserGreen else NeonAmber,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = purchaseFeedback ?: "",
                            color = SpaceTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (!isPurchasing) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DISMISS",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { onClearFeedback() }
                                .padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinPackageCard(
    pkg: CoinPackage,
    isTestPaymentMode: Boolean,
    isPurchasing: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        pkg.isBestValue -> NeonGold
        pkg.isPopular -> CyberCyan
        pkg.badge != null -> Color(0xFF38BDF8)
        else -> SpaceCardBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.2.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = !isPurchasing, onClick = onClick)
            .testTag("package_${pkg.productId}"),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional Badge
            if (pkg.badge != null) {
                Surface(
                    color = if (pkg.isBestValue) NeonGold else CyberCyan,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = pkg.badge,
                        color = Color(0xFF060913),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Coin Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF1E293B), CircleShape)
                    .border(1.dp, NeonGold.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = NeonGold,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Coin Amount
            Text(
                text = "%,d".format(pkg.coinAmount),
                color = SpaceTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "COINS",
                color = NeonGold,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price Button Style Pill
            Surface(
                color = if (isTestPaymentMode) Color(0xFF78350F) else Color(0xFF00363D),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, if (isTestPaymentMode) NeonAmber else CyberCyan),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = pkg.formattedPrice,
                    color = if (isTestPaymentMode) NeonAmber else CyberCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pkg.productId,
                color = SpaceTextMuted,
                fontSize = 9.sp
            )
        }
    }
}
