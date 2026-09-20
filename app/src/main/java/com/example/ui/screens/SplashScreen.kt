package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TobiDiamond3D
import com.example.ui.theme.DiamondBg
import com.example.ui.theme.DiamondBorder
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, animationSpec = tween(450))
        delay(1150)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        SpaceDarkBg,
                        Color(0xFF0C1424),
                        SpaceDarkBg
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Subtle ambient neon diamond glow
        Canvas(modifier = Modifier.size(320.dp)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DiamondCyan.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.minDimension / 2f
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Modern 3D Diamond Emblem with Dark Card Frame
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .shadow(24.dp, CircleShape, spotColor = DiamondCyan.copy(alpha = 0.4f))
                    .background(SpaceCardBg, CircleShape)
                    .border(2.dp, DiamondBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                TobiDiamond3D(size = 72.dp, animated = true)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Title
            Text(
                text = "TOBI GT",
                color = SpaceTextPrimary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 7.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Developer Subtitle
            Text(
                text = "Developed by Tobi",
                color = DiamondCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Edition pill
            Surface(
                color = SpaceCardElevated,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiamondBorder)
            ) {
                Text(
                    text = "DIAMOND EDITION • ACE PILOT",
                    color = SpaceTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(42.dp))

            CircularProgressIndicator(
                color = DiamondCyan,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.5.dp
            )
        }
    }
}
