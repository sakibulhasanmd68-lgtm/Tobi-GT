package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.components.CyberButton
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
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
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Status Bar: Pilot Badge, High Score, Coin Balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Anonymous Pilot Callsign Badge
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SpaceCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(CyberCyan, CircleShape)
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

                // Coins Balance Display
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGold.copy(alpha = 0.7f)),
                    modifier = Modifier.shadow(6.dp, RoundedCornerShape(20.dp), spotColor = NeonGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = NeonGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "%,d".format(coins),
                            color = NeonGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tobi GT Futuristic Logo & Fighter Insignia
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFF0C1322), CircleShape)
                    .border(2.dp, CyberCyan.copy(alpha = glowAlpha), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(64.dp)) {
                    val w = size.width
                    val h = size.height

                    val jetPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        lineTo(w * 0.6f, h * 0.4f)
                        lineTo(w * 0.9f, h * 0.65f)
                        lineTo(w * 0.65f, h * 0.72f)
                        lineTo(w * 0.5f, h * 0.6f)
                        lineTo(w * 0.35f, h * 0.72f)
                        lineTo(w * 0.1f, h * 0.65f)
                        lineTo(w * 0.4f, h * 0.4f)
                        close()
                    }
                    drawPath(jetPath, color = CyberCyan)

                    val cockpitPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.28f)
                        lineTo(w * 0.54f, h * 0.44f)
                        lineTo(w * 0.5f, h * 0.5f)
                        lineTo(w * 0.46f, h * 0.44f)
                        close()
                    }
                    drawPath(cockpitPath, color = NeonGold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "TOBI GT",
                color = CyberCyan,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )

            Text(
                text = "FUTURISTIC AIR COMBAT",
                color = SpaceTextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.5.sp
            )

            // High Score Banner
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = Color(0xFF131F38),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A8A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = NeonGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ALL-TIME HIGH SCORE:  ",
                        color = SpaceTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "%,d".format(highScore),
                        color = NeonGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // PRIMARY ACTION: PLAY
            CyberButton(
                text = "DEPLOY BATTLE (PLAY)",
                onClick = { onNavigate(AppScreen.GAME) },
                icon = Icons.Default.PlayArrow,
                isPrimary = true,
                modifier = Modifier.height(60.dp),
                testTag = "home_play_button"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // NEW FEATURE HIGHLIGHT: PLAY POINTS / REWARDS BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, Brush.horizontalGradient(listOf(Color(0xFF34D399), Color(0xFF00F0FF))), RoundedCornerShape(14.dp))
                    .clickable { onNavigate(AppScreen.PLAY_POINTS) }
                    .testTag("home_play_points_banner"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B).copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF059669), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Loyalty,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PLAY POINTS / REWARDS",
                                color = SpaceTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFF10B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "NEW",
                                    color = Color.Black,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Use Google Play Points for Tobi GT coupons",
                            color = Color(0xFF6EE7B7),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Menu Grid (COIN STORE, MISSIONS, LEADERBOARD, PROFILE)
            Row(modifier = Modifier.fillMaxWidth()) {
                HomeMenuCard(
                    title = "COIN STORE",
                    subtitle = "19 Value Packs",
                    icon = Icons.Default.ShoppingCart,
                    iconTint = NeonGold,
                    modifier = Modifier.weight(1f),
                    testTag = "home_coin_store_card",
                    onClick = { onNavigate(AppScreen.COIN_STORE) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                HomeMenuCard(
                    title = "MISSIONS",
                    subtitle = "Combat Bounties",
                    icon = Icons.Default.Assignment,
                    iconTint = CyberCyan,
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
                    iconTint = NeonAmber,
                    modifier = Modifier.weight(1f),
                    testTag = "home_leaderboard_card",
                    onClick = { onNavigate(AppScreen.LEADERBOARD) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                HomeMenuCard(
                    title = "PROFILE",
                    subtitle = "Pilot Logbook",
                    icon = Icons.Default.Person,
                    iconTint = Color(0xFFA855F7),
                    modifier = Modifier.weight(1f),
                    testTag = "home_profile_card",
                    onClick = { onNavigate(AppScreen.PROFILE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secondary Options: SETTINGS, PRIVACY POLICY, CONTACT
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SecondaryMenuRow(
                    title = "SETTINGS",
                    icon = Icons.Default.Settings,
                    testTag = "home_settings_btn",
                    onClick = { onNavigate(AppScreen.SETTINGS) }
                )
                SecondaryMenuRow(
                    title = "PRIVACY POLICY",
                    icon = Icons.Default.Policy,
                    testTag = "home_privacy_btn",
                    onClick = { onNavigate(AppScreen.PRIVACY_POLICY) }
                )
                SecondaryMenuRow(
                    title = "CONTACT SUPPORT",
                    icon = Icons.Default.ContactSupport,
                    testTag = "home_contact_btn",
                    onClick = { onNavigate(AppScreen.CONTACT) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Anonymous System Footer Notice
            Text(
                text = "ANONYMOUS PILOT SYSTEM — NO LOGIN REQUIRED",
                color = SpaceTextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun HomeMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
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
        color = SpaceCardBg.copy(alpha = 0.6f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SpaceCardBorder.copy(alpha = 0.5f)),
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
                tint = SpaceTextSecondary,
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
