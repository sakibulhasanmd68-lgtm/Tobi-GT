package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberButton
import com.example.ui.components.TobiDiamond3D
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.DiamondLight
import com.example.ui.theme.LaserGreen
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary

@Composable
fun ContactScreen(
    coins: Long,
    userId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("contact_screen")
    ) {
        TobiGtHeader(
            title = "DEVELOPER CONTACT",
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
                        .clip(RoundedCornerShape(18.dp))
                        .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x33000000))
                        .border(1.2.dp, DiamondBorder, RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(DiamondBg, SpaceCardBg)
                                )
                            )
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(SpaceCardElevated, CircleShape)
                                .border(1.5.dp, DiamondCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = DiamondCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "TOBI GT",
                            color = SpaceTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            text = "Developed by Tobi",
                            color = DiamondCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "For support inquiries, questions about diamond fulfillment, refund assistance, or bug reports, email our developer directly.",
                            color = SpaceTextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )

                        // Anonymous Pilot Reference ID
                        Surface(
                            color = SpaceCardElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SpaceCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "YOUR ANONYMOUS PILOT ID:",
                                    color = DiamondCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = userId.ifEmpty { "Generating Pilot ID..." },
                                    color = SpaceTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Developer Email Card
                        Surface(
                            color = SpaceCardElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, DiamondBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = DiamondCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "DEVELOPER EMAIL",
                                        color = SpaceTextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "sakibulhasanmd69@gmail.com",
                                        color = SpaceTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        CyberButton(
                            text = "SEND SUPPORT EMAIL",
                            onClick = {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:sakibulhasanmd69@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "[TOBI GT Support] Pilot ID: $userId")
                                }
                                try {
                                    context.startActivity(emailIntent)
                                } catch (_: Exception) {
                                    // Fallback if no email client
                                }
                            }
                        )
                    }
                }
            }

            // FAQs
            item {
                Text(
                    text = "FREQUENTLY ASKED QUESTIONS",
                    color = DiamondCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            item {
                FaqCard(
                    question = "How are Diamond purchases credited?",
                    answer = "Purchases are securely fulfilled via Google Play Billing. As soon as the transaction is confirmed, diamonds are added immediately to your balance and permanently recorded in your local transaction ledger."
                )
            }

            item {
                FaqCard(
                    question = "How do Google Play Points work in Tobi GT?",
                    answer = "Eligible players can redeem Play Points for Tobi GT discount coupons in Google Play. The discount is automatically deducted when completing your in-game purchase."
                )
            }

            item {
                FaqCard(
                    question = "What if my purchase doesn't arrive?",
                    answer = "Tap 'Transactions' in the menu to view all verified orders. If a purchase is missing, email sakibulhasanmd69@gmail.com with your Pilot ID and Google Play order receipt for immediate manual audit."
                )
            }
        }
    }
}

@Composable
private fun FaqCard(question: String, answer: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .shadow(1.dp, RoundedCornerShape(12.dp), spotColor = Color(0x10000000))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = question, color = DiamondCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = answer, color = SpaceTextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
        }
    }
}
