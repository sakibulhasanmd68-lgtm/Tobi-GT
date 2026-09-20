package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary

@Composable
fun PrivacyPolicyScreen(
    coins: Long,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("privacy_policy_screen")
    ) {
        TobiGtHeader(
            title = "PRIVACY POLICY",
            coins = coins,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                PolicyCard(
                    title = "1. Anonymous User Architecture",
                    body = "Tobi GT operates with zero registration and zero login requirements. We do not collect your real name, email address, phone number, contacts, or passwords. Your gameplay progress and virtual Coin inventory are associated exclusively with a randomly generated internal anonymous identifier."
                )
            }

            item {
                PolicyCard(
                    title = "2. Data Safety & Local Storage",
                    body = "Game telemetry, high scores, combat statistics, and authoritative transaction receipts are stored securely within your device's local encrypted Room database. When you back up your device, this data is safeguarded in accordance with Android system standards."
                )
            }

            item {
                PolicyCard(
                    title = "3. In-App Purchases & Financial Data",
                    body = "All commercial payments are processed exclusively through Google Play Billing. Tobi GT never collects, receives, or stores your credit card, debit card, bank information, or Google account password. Google Play communicates purchase verification tokens to enable coin delivery."
                )
            }

            item {
                PolicyCard(
                    title = "4. Google Play Points Rewards",
                    body = "Google Play Points is an independent rewards program operated solely by Google LLC. Tobi GT does not possess direct access to your Google Play Points account balance. Coupon redemptions and point exchanges are governed by Google's Terms of Service."
                )
            }

            item {
                PolicyCard(
                    title = "5. Contact Information",
                    body = "If you have questions regarding this Privacy Policy or your in-game anonymous account data, contact our support team at: support@tobigtgame.com"
                )
            }
        }
    }
}

@Composable
private fun PolicyCard(title: String, body: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = CyberCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = body, color = SpaceTextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}
