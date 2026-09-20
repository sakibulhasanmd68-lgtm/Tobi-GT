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
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.ui.components.TobiDiamond3D
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondLight
import com.example.ui.theme.LaserGreen
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceCardElevated
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
    onNavigateTransactions: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    var selectedPackage by remember { mutableStateOf<CoinPackage?>(null) }
    var activeCategory by remember { mutableStateOf("ALL") }

    val filteredPackages = remember(activeCategory) {
        when (activeCategory) {
            "POPULAR" -> CoinPackages.ALL.filter { it.isPopular || it.badge == "PILOT'S PICK" || it.badge == "SQUADRON" }
            "VAULT" -> CoinPackages.ALL.filter { it.priceDollars >= 50.0 || it.isBestValue }
            else -> CoinPackages.ALL
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("coin_store_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with Live Diamond Balance
            TobiGtHeader(
                title = "DIAMOND SHOP",
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
                // Large Hero Showcase & Filter Chips
                item(span = { GridItemSpan(2) }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Hero Diamond Showcase Banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = DiamondCyan.copy(alpha = 0.3f))
                                .border(1.5.dp, DiamondBorder, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(DiamondBg, SpaceCardBg)
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Surface(
                                            color = Color(0x3300E5FF),
                                            shape = RoundedCornerShape(6.dp),
                                            border = BorderStroke(1.dp, Color(0x6600E5FF))
                                        ) {
                                            Text(
                                                text = "OFFICIAL DIAMOND VAULT",
                                                color = DiamondCyan,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 1.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Tobi GT Diamonds",
                                            color = SpaceTextPrimary,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "1 USD = 100 Diamonds\nCalculated instantly on checkout",
                                            color = SpaceTextSecondary,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }

                                    // Large 3D Faceted Diamond Visual
                                    TobiDiamond3D(size = 76.dp, animated = true)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Google Play Points Rewards Callout Banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.2.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                                .clickable { onNavigatePlayPoints() }
                                .testTag("store_play_points_callout"),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF062E22))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFF10B981), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Loyalty,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Google Play Points Eligible",
                                        color = Color(0xFF34D399),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Exchange Play Points for $1 to $40 OFF discounts",
                                        color = Color(0xFFA7F3D0),
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = "REWARDS →",
                                    color = DiamondCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Filter Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StoreFilterChip(
                                label = "ALL (${CoinPackages.ALL.size})",
                                isSelected = activeCategory == "ALL",
                                onClick = { activeCategory = "ALL" },
                                modifier = Modifier.weight(1f)
                            )
                            StoreFilterChip(
                                label = "POPULAR",
                                isSelected = activeCategory == "POPULAR",
                                onClick = { activeCategory = "POPULAR" },
                                modifier = Modifier.weight(1f)
                            )
                            StoreFilterChip(
                                label = "VAULT",
                                isSelected = activeCategory == "VAULT",
                                onClick = { activeCategory = "VAULT" },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Provider Toggle (Play Billing vs Dev Test Provider)
                        Surface(
                            color = SpaceCardBg,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SpaceCardBorder),
                            shadowElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = if (isTestPaymentMode) NeonAmber else DiamondCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isTestPaymentMode) "DEV TEST PAYMENT PIPELINE" else "GOOGLE PLAY BILLING",
                                            color = if (isTestPaymentMode) NeonAmber else DiamondCyan,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isTestPaymentMode) "Verifies token & records local ledger" else "Google Play official billing service",
                                            color = SpaceTextMuted,
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                Switch(
                                    checked = isTestPaymentMode,
                                    onCheckedChange = { onToggleTestPayment() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = NeonAmber,
                                        checkedTrackColor = Color(0xFF78350F),
                                        uncheckedThumbColor = DiamondCyan,
                                        uncheckedTrackColor = DiamondBg
                                    ),
                                    modifier = Modifier.testTag("payment_mode_switch")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                // 20 Diamond Packages Grid
                items(filteredPackages) { pkg ->
                    val isSelected = selectedPackage?.productId == pkg.productId
                    DiamondPackageCard(
                        pkg = pkg,
                        isSelected = isSelected,
                        isTestPaymentMode = isTestPaymentMode,
                        isPurchasing = isPurchasing,
                        onClick = {
                            selectedPackage = pkg
                            onPurchasePackage(pkg)
                        }
                    )
                }
            }
        }

        // ============================================================
        // PURCHASE STATUS & FEEDBACK FLOATING PANEL
        // ============================================================
        AnimatedVisibility(
            visible = purchaseFeedback != null || isPurchasing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = SpaceCardElevated,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                border = BorderStroke(1.5.dp, DiamondCyan),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("purchase_feedback_panel")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isPurchasing) {
                                CircularProgressIndicator(
                                    color = DiamondCyan,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                val isSuccess = purchaseFeedback?.contains("SUCCESS", ignoreCase = true) == true
                                Icon(
                                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = if (isSuccess) LaserGreen else NeonAmber,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isPurchasing) "VERIFYING GOOGLE PLAY PURCHASE..." else "PURCHASE STATUS",
                                    color = SpaceTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = purchaseFeedback ?: "Processing digital order securely...",
                                    color = SpaceTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        if (!isPurchasing) {
                            IconButton(
                                onClick = onClearFeedback,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = SpaceTextSecondary
                                )
                            }
                        }
                    }

                    if (!isPurchasing && onNavigateTransactions != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateTransactions() }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = DiamondCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "VIEW IN TRANSACTION HISTORY",
                                color = DiamondCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) DiamondCyan else SpaceCardBg,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isSelected) DiamondCyan else SpaceCardBorder),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        modifier = modifier
            .height(34.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) Color(0xFF090D16) else SpaceTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun DiamondPackageCard(
    pkg: CoinPackage,
    isSelected: Boolean,
    isTestPaymentMode: Boolean,
    isPurchasing: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> DiamondCyan
        pkg.isBestValue -> NeonGold
        pkg.isPopular -> DiamondCyan
        pkg.badge != null -> DiamondLight
        else -> SpaceCardBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = if (isSelected) 8.dp else 3.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isSelected) DiamondCyan else Color.Black
            )
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = !isPurchasing, onClick = onClick)
            .testTag("package_${pkg.productId}"),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Optional Badge Header
            if (pkg.badge != null) {
                Surface(
                    color = if (pkg.isBestValue) NeonGold else Color(0xFF0284C7),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = pkg.badge,
                        color = if (pkg.isBestValue) Color(0xFF1E1B4B) else Color.White,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(18.dp))
            }

            // 3D Diamond Visual
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(4.dp, CircleShape, spotColor = DiamondCyan.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                TobiDiamond3D(size = 42.dp, animated = isSelected)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Diamond Quantity (e.g., 501 Diamonds)
            Text(
                text = "%,d".format(pkg.diamondAmount),
                color = SpaceTextPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "DIAMONDS",
                color = DiamondCyan,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Price Pill Button
            val priceGradient = if (isSelected) {
                Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFF0284C7)))
            } else {
                Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF172033)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(priceGradient)
                    .border(1.dp, if (isSelected) DiamondCyan else SpaceCardBorder, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pkg.formattedPrice,
                    color = if (isSelected) Color(0xFF090D16) else SpaceTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pkg.productId,
                color = SpaceTextMuted,
                fontSize = 8.sp
            )
        }
    }
}
