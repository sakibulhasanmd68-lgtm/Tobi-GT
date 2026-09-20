package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, animationSpec = tween(500))
        delay(1200)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // Futuristic Jet Insignia Canvas
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(Color(0xFF0F172A), CircleShape)
                    .border(2.dp, CyberCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(90.dp)) {
                    val w = size.width
                    val h = size.height

                    // Jet body
                    val jetPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.1f)
                        lineTo(w * 0.58f, h * 0.38f)
                        lineTo(w * 0.88f, h * 0.65f)
                        lineTo(w * 0.62f, h * 0.72f)
                        lineTo(w * 0.5f, h * 0.62f)
                        lineTo(w * 0.38f, h * 0.72f)
                        lineTo(w * 0.12f, h * 0.65f)
                        lineTo(w * 0.42f, h * 0.38f)
                        close()
                    }
                    drawPath(jetPath, color = CyberCyan)

                    // Cockpit gold
                    val cockpitPath = Path().apply {
                        moveTo(w * 0.5f, h * 0.25f)
                        lineTo(w * 0.54f, h * 0.42f)
                        lineTo(w * 0.5f, h * 0.48f)
                        lineTo(w * 0.46f, h * 0.42f)
                        close()
                    }
                    drawPath(cockpitPath, color = NeonGold)

                    // Thruster plume
                    drawLine(
                        color = Color(0xFF38BDF8),
                        start = Offset(w * 0.5f, h * 0.66f),
                        end = Offset(w * 0.5f, h * 0.88f),
                        strokeWidth = 5f
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "TOBI GT",
                color = CyberCyan,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp
            )

            Text(
                text = "SUPRASONIC SKY BATTLE",
                color = NeonGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            CircularProgressIndicator(
                color = CyberCyan,
                modifier = Modifier.size(28.dp),
                strokeWidth = 2.5.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "INITIALIZING SKY SYSTEMS...",
                color = SpaceTextMuted,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
            )
        }
    }
}
