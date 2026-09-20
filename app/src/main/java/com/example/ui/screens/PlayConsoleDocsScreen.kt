package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
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
fun PlayConsoleDocsScreen(
    coins: Long,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("play_console_docs_screen")
    ) {
        TobiGtHeader(
            title = "PLAY CONSOLE SETUP",
            coins = coins,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(2.dp, RoundedCornerShape(16.dp), spotColor = Color(0x15000000))
                        .border(1.dp, DiamondBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Loyalty,
                                contentDescription = null,
                                tint = DiamondCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Google Play Points Integration Guide",
                                color = SpaceTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Step-by-step developer instructions for configuring official Play Points coupon promotions in Google Play Console for TOBI GT.",
                            color = SpaceTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            item {
                DocSectionCard(
                    stepNumber = "1",
                    title = "Console Navigation",
                    body = "1. Sign in to your Google Play Console account.\n2. Select the TOBI GT application (package: com.aistudio.tobigt.skyg).\n3. In the left-hand navigation menu, scroll down to the 'Monetize with Play' section.\n4. Click 'Products' → Select 'Play Points'."
                )
            }

            item {
                DocSectionCard(
                    stepNumber = "2",
                    title = "Promotion Type Selection",
                    body = "1. Click 'Create promotion'.\n2. Under Promotion Type, select 'Coupon promotion'.\n3. This allows Google Play users to spend their accrued Google Play Points to receive discount coupons redeemable on TOBI GT in-app Diamond packages."
                )
            }

            item {
                DocSectionCard(
                    stepNumber = "3",
                    title = "Configuring the 12 Coupon Values",
                    body = "Configure promotions for each desired tier:\n• $1 OFF (1 Play Point)\n• $2 OFF (2 Play Points)\n• $3 OFF (3 Play Points)\n• $4 OFF (4 Play Points)\n• $5 OFF (5 Play Points)\n• $6 OFF (6 Play Points)\n• $10 OFF (10 Play Points)\n• $15 OFF (15 Play Points)\n• $20 OFF (20 Play Points)\n• $25 OFF (25 Play Points)\n• $30 OFF (30 Play Points)\n• $40 OFF (40 Play Points)"
                )
            }

            item {
                DocSectionCard(
                    stepNumber = "4",
                    title = "Product Association & Minimum Purchase",
                    body = "• Associate coupons with eligible Diamond SKUs (e.g. tobi_coins_101 to tobi_coins_100001).\n• Set Minimum Purchase threshold if applicable (e.g., a $5 coupon requires a purchase of at least $5.01, perfectly pairing with the $5.01 = 501 Diamonds package).\n• Note that Google Play enforces coupon application at Google Play checkout automatically."
                )
            }

            item {
                DocSectionCard(
                    stepNumber = "5",
                    title = "Targeting & Duration Settings",
                    body = "• Country / Region: Select all supported territories (US, Japan, Korea, UK, Germany, etc. where Google Play Points program is active).\n• Promotion Duration: Choose Start Date and End Date (e.g., quarterly or continuous seasonal campaign).\n• Redemption Limits: Set per-user redemption limit (e.g., 1 per month or unlimited)."
                )
            }

            item {
                DocSectionCard(
                    stepNumber = "6",
                    title = "Google Play Purchase & Verification Flow",
                    body = "1. User redeems coupon in Google Play Store Points tab.\n2. In TOBI GT Diamond Store, user taps on an in-app package.\n3. Google Play Billing purchase bottom sheet opens.\n4. Google Play automatically presents the discount coupon to the user.\n5. Upon completion, Google Play returns standard purchaseToken and orderId.\n6. TOBI GT backend verifies receipt, performs duplicate check, credits full authoritative Diamonds, and logs transaction."
                )
            }
        }
    }
}

@Composable
private fun DocSectionCard(
    stepNumber: String,
    title: String,
    body: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .shadow(1.dp, RoundedCornerShape(12.dp), spotColor = Color(0x10000000))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(DiamondBg, CircleShape)
                        .border(1.dp, DiamondCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        color = DiamondCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = SpaceTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = body,
                color = SpaceTextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

