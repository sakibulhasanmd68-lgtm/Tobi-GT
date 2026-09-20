package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminAuditLogEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import com.example.data.repository.AdminSummaryStats
import com.example.ui.components.CyberButton
import com.example.ui.components.TobiGtHeader
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.LaserGreen
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCrimson
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    user: UserEntity?,
    stats: AdminSummaryStats?,
    transactions: List<TransactionEntity>,
    auditLogs: List<AdminAuditLogEntity>,
    onAdjustCoins: (userId: String, newCoins: Long, reason: String) -> Unit,
    onRefundTransaction: (txId: Long, reason: String) -> Unit,
    onBack: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    var targetUserId by remember { mutableStateOf(user?.userId ?: "") }
    var adjustCoinsAmount by remember { mutableStateOf("10000") }
    var adjustReason by remember { mutableStateOf("Customer service loyalty bonus") }
    var adjustmentFeedback by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("admin_dashboard_screen")
    ) {
        TobiGtHeader(
            title = "ADMIN COMMAND",
            coins = user?.coins ?: 0L,
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Security Badge
            item {
                Surface(
                    color = DiamondBg,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DiamondBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = DiamondCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "AUTHORITATIVE SYSTEM AUDIT & LEDGER",
                                color = SpaceTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "All diamond adjustments and refunds are permanently written to the audit log",
                                color = SpaceTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // High Level Metrics
            item {
                Text(
                    text = "ECONOMY & REVENUE METRICS",
                    color = SpaceTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AdminMetricBox(
                        title = "TOTAL USERS",
                        value = "${stats?.totalUsers ?: 0}",
                        icon = Icons.Default.People,
                        tint = DiamondCyan,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AdminMetricBox(
                        title = "DIAMONDS CIRCULATION",
                        value = "%,d".format(stats?.totalCoinsCirculation ?: 0L),
                        icon = Icons.Default.MonetizationOn,
                        tint = DiamondCyan,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AdminMetricBox(
                        title = "TRANSACTIONS",
                        value = "${stats?.totalTransactions ?: 0} (${stats?.testTransactions ?: 0} Test)",
                        icon = Icons.Default.Receipt,
                        tint = SpaceTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AdminMetricBox(
                        title = "REAL REVENUE",
                        value = "$${"%.2f".format(stats?.totalRealRevenue ?: 0.0)}",
                        icon = Icons.Default.AttachMoney,
                        tint = LaserGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Diamond Adjustment Tool
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .shadow(1.dp, RoundedCornerShape(14.dp), spotColor = Color(0x10000000))
                        .border(1.dp, SpaceCardBorder, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MANUAL DIAMOND BALANCE ADJUSTMENT",
                            color = DiamondCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = targetUserId,
                            onValueChange = { targetUserId = it },
                            label = { Text("Target User ID") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DiamondCyan,
                                unfocusedBorderColor = SpaceCardBorder,
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = adjustCoinsAmount,
                            onValueChange = { adjustCoinsAmount = it },
                            label = { Text("New Diamond Balance") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DiamondCyan,
                                unfocusedBorderColor = SpaceCardBorder,
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = adjustReason,
                            onValueChange = { adjustReason = it },
                            label = { Text("Audit Reason") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DiamondCyan,
                                unfocusedBorderColor = SpaceCardBorder,
                                focusedTextColor = SpaceTextPrimary,
                                unfocusedTextColor = SpaceTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        CyberButton(
                            text = "APPLY DIAMOND ADJUSTMENT",
                            onClick = {
                                val amount = adjustCoinsAmount.toLongOrNull()
                                if (amount != null && targetUserId.isNotBlank()) {
                                    onAdjustCoins(targetUserId, amount, adjustReason)
                                    adjustmentFeedback = "Successfully updated $targetUserId balance to $amount diamonds"
                                }
                            },
                            isPrimary = true,
                            testTag = "admin_apply_coins_btn"
                        )

                        if (adjustmentFeedback != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = adjustmentFeedback ?: "",
                                color = LaserGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Recent Transactions & Refund Control
            item {
                Text(
                    text = "LEDGER TRANSACTIONS (${transactions.size})",
                    color = SpaceTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(transactions.take(15)) { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .shadow(1.dp, RoundedCornerShape(10.dp), spotColor = Color(0x10000000))
                        .border(1.dp, SpaceCardBorder, RoundedCornerShape(10.dp)),
                    colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${tx.packageName} (${tx.productId})", color = SpaceTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = "$${"%.2f".format(tx.price)}", color = DiamondCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "Order: ${tx.orderId} • ${tx.paymentProvider}", color = SpaceTextMuted, fontSize = 10.sp)
                        Text(text = "User: ${tx.anonymousUserId}", color = SpaceTextSecondary, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Status: ${tx.paymentStatus}", color = if (tx.paymentStatus == "SUCCESS") LaserGreen else NeonCrimson, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            if (tx.paymentStatus == "SUCCESS") {
                                Surface(
                                    color = Color(0xFF450A0A),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, NeonCrimson.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .clickable { onRefundTransaction(tx.id, "Admin requested refund") }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "REFUND & DEDUCT", color = NeonCrimson, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }

            // Audit Logs
            item {
                Text(
                    text = "AUDIT LOGS (${auditLogs.size})",
                    color = SpaceTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(auditLogs.take(15)) { log ->
                Surface(
                    color = SpaceCardElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SpaceCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = log.adminAction, color = DiamondCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = dateFormat.format(Date(log.timestamp)), color = SpaceTextMuted, fontSize = 9.sp)
                        }
                        Text(text = "Target: ${log.targetUserId}", color = SpaceTextSecondary, fontSize = 10.sp)
                        Text(text = "${log.previousValue} → ${log.newValue}", color = SpaceTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Reason: ${log.reason}", color = SpaceTextMuted, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricBox(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .shadow(1.dp, RoundedCornerShape(12.dp), spotColor = Color(0x10000000))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SpaceCardBg)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, color = SpaceTextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = SpaceTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
        }
    }
}

