package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.components.CyberButton
import com.example.ui.components.TobiDiamond3D
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBlue
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondLight
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary

@Composable
fun HomeScreen(
    coins: Long,
    highScore: Int,
    pilotName: String,
    onNavigate: (AppScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("home_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Status Bar: Pilot Badge & Quick Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Anonymous Pilot Callsign Badge
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SpaceCardBorder),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(DiamondCyan, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pilotName,
                            color = SpaceTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // High Score Badge
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SpaceCardBorder),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NeonGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "SCORE: %,d".format(highScore),
                            color = NeonGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tobi GT Futuristic Combat Jet Emblem
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .shadow(12.dp, CircleShape, spotColor = DiamondCyan.copy(alpha = 0.6f))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        ),
                        CircleShape
                    )
                    .border(1.5.dp, DiamondBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(56.dp)) {
                    val w = size.width
                    val h = size.height

                    val jetPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.12f)
                        lineTo(w * 0.6f, h * 0.42f)
                        lineTo(w * 0.88f, h * 0.65f)
                        lineTo(w * 0.64f, h * 0.70f)
                        lineTo(w * 0.5f, h * 0.60f)
                        lineTo(w * 0.36f, h * 0.70f)
                        lineTo(w * 0.12f, h * 0.65f)
                        lineTo(w * 0.4f, h * 0.42f)
                        close()
                    }
                    drawPath(jetPath, color = DiamondCyan)

                    val cockpitPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.26f)
                        lineTo(w * 0.54f, h * 0.42f)
                        lineTo(w * 0.5f, h * 0.48f)
                        lineTo(w * 0.46f, h * 0.42f)
                        close()
                    }
                    drawPath(cockpitPath, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // TOBI GT Title
            Text(
                text = "TOBI GT",
                color = SpaceTextPrimary,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )

            // Developed by Tobi
            Text(
                text = "Developed by Tobi",
                color = DiamondCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ============================================================
            // PROMINENT CURRENT DIAMOND BALANCE CARD (USER SPEC REQUIREMENT)
            // ============================================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = DiamondCyan.copy(alpha = 0.35f))
                    .border(1.5.dp, DiamondBorder, RoundedCornerShape(20.dp))
                    .clickable { onNavigate(AppScreen.COIN_STORE) }
                    .testTag("home_diamond_balance_card"),
                colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    DiamondBg,
                                    SpaceCardBg
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(DiamondCyan, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CURRENT DIAMOND BALANCE",
                                    color = DiamondLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.2.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "%,d".format(coins),
                                    color = SpaceTextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Diamonds",
                                    color = DiamondCyan,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = Color(0x3300E5FF),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, Color(0x5500E5FF))
                            ) {
                                Text(
                                    text = "TAP TO OPEN DIAMOND SHOP",
                                    color = DiamondCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // 3D Faceted Crystal Diamond Visual
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .shadow(8.dp, CircleShape, spotColor = DiamondCyan.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            TobiDiamond3D(size = 64.dp, animated = true)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ============================================================
            // PRIMARY ACTION BUTTONS (BUY DIAMONDS, GOOGLE PLAY POINTS REWARDS)
            // ============================================================
            CyberButton(
                text = "BUY DIAMONDS",
                onClick = { onNavigate(AppScreen.COIN_STORE) },
                icon = Icons.Default.ShoppingCart,
                badge = "20 PACKS",
                isPrimary = true,
                modifier = Modifier.height(54.dp),
                testTag = "home_buy_diamonds_button"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dedicated Google Play Points Rewards Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = Color(0x3310B981))
                    .border(1.2.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { onNavigate(AppScreen.PLAY_POINTS) }
                    .testTag("home_play_points_banner"),
                colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF062E22), Color(0xFF111827))
                            )
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF10B981), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Loyalty,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "GOOGLE PLAY POINTS REWARDS",
                                color = Color(0xFF34D399),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFF10B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "12 TIERS",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Exchange Play Points for $1 to $40 Diamond discounts",
                            color = Color(0xFFA7F3D0),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // DEPLOY BATTLE (PLAY) Secondary Action
            CyberButton(
                text = "DEPLOY BATTLE (PLAY GAME)",
                onClick = { onNavigate(AppScreen.GAME) },
                icon = Icons.Default.PlayArrow,
                isPrimary = false,
                modifier = Modifier.height(50.dp),
                testTag = "home_play_button"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Grid: TRANSACTION HISTORY, MISSIONS, LEADERBOARD, SETTINGS
            Row(modifier = Modifier.fillMaxWidth()) {
                HomeMenuCard(
                    title = "TRANSACTIONS",
                    subtitle = "Purchase History",
                    icon = Icons.AutoMirrored.Filled.ReceiptLong,
                    iconTint = DiamondCyan,
                    modifier = Modifier.weight(1f),
                    testTag = "home_transactions_card",
                    onClick = { onNavigate(AppScreen.PROFILE) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                HomeMenuCard(
                    title = "MISSIONS",
                    subtitle = "Combat Bounties",
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    iconTint = DiamondLight,
                    modifier = Modifier.weight(1f),
                    testTag = "home_missions_card",
                    onClick = { onNavigate(AppScreen.MISSIONS) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                HomeMenuCard(
                    title = "LEADERBOARD",
                    subtitle = "Top Sky Aces",
                    icon = Icons.Default.EmojiEvents,
                    iconTint = NeonGold,
                    modifier = Modifier.weight(1f),
                    testTag = "home_leaderboard_card",
                    onClick = { onNavigate(AppScreen.LEADERBOARD) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                HomeMenuCard(
                    title = "SETTINGS",
                    subtitle = "System & Sound",
                    icon = Icons.Default.Settings,
                    iconTint = SpaceTextSecondary,
                    modifier = Modifier.weight(1f),
                    testTag = "home_settings_btn",
                    onClick = { onNavigate(AppScreen.SETTINGS) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Support Links: HELP & SUPPORT, PRIVACY POLICY
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryMenuRow(
                    title = "HELP & SUPPORT CENTER",
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    testTag = "home_contact_btn",
                    onClick = { onNavigate(AppScreen.CONTACT) }
                )
                SecondaryMenuRow(
                    title = "PRIVACY POLICY",
                    icon = Icons.Default.Policy,
                    testTag = "home_privacy_btn",
                    onClick = { onNavigate(AppScreen.PRIVACY_POLICY) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Developed by Tobi & Anonymous Notice Footer
            Text(
                text = "TOBI GT — DEVELOPED BY TOBI",
                color = SpaceTextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "SECURE GOOGLE PLAY PURCHASES • NO REGISTRATION REQUIRED",
                color = SpaceTextMuted.copy(alpha = 0.7f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
private fun HomeMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    iconText: String? = null,
    iconTint: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .shadow(3.dp, RoundedCornerShape(14.dp), spotColor = Color(0x33000000))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (iconText != null) {
                Text(text = iconText, fontSize = 22.sp)
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = SpaceTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = SpaceTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun SecondaryMenuRow(
    title: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        color = SpaceCardElevated,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, SpaceCardBorder),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DiamondCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = SpaceTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
