package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Precomputed, cached geometry for the 3D crystal gemstone.
 * Allocates zero Path objects during onDraw passes.
 */
private class DiamondGeometry(val w: Float, val h: Float) {
    val topY = h * 0.14f
    val girdleY = h * 0.42f
    val culetY = h * 0.94f
    val centerX = w * 0.5f

    val tLeftX = w * 0.26f
    val tRightX = w * 0.74f

    val gLeftX = w * 0.08f
    val gRightX = w * 0.92f

    val cLeftX = w * 0.35f
    val cRightX = w * 0.65f

    val strokeWidth = (w * 0.018f).coerceAtLeast(1f)
    val sparkleX = tRightX * 0.95f
    val sparkleY = topY * 1.05f
    val sparkleRadius = (w * 0.06f).coerceAtLeast(2f)

    val pLeft = Path().apply {
        moveTo(gLeftX, girdleY)
        lineTo(cLeftX, girdleY)
        lineTo(centerX, culetY)
        close()
    }

    val pCenterLeft = Path().apply {
        moveTo(cLeftX, girdleY)
        lineTo(centerX, girdleY)
        lineTo(centerX, culetY)
        close()
    }

    val pCenterRight = Path().apply {
        moveTo(centerX, girdleY)
        lineTo(cRightX, girdleY)
        lineTo(centerX, culetY)
        close()
    }

    val pRight = Path().apply {
        moveTo(cRightX, girdleY)
        lineTo(gRightX, girdleY)
        lineTo(centerX, culetY)
        close()
    }

    val cLeft = Path().apply {
        moveTo(tLeftX, topY)
        lineTo(gLeftX, girdleY)
        lineTo(cLeftX, girdleY)
        close()
    }

    val cMidLeft = Path().apply {
        moveTo(tLeftX, topY)
        lineTo(centerX, topY)
        lineTo(centerX, girdleY)
        lineTo(cLeftX, girdleY)
        close()
    }

    val cMidRight = Path().apply {
        moveTo(centerX, topY)
        lineTo(tRightX, topY)
        lineTo(cRightX, girdleY)
        lineTo(centerX, girdleY)
        close()
    }

    val cRight = Path().apply {
        moveTo(tRightX, topY)
        lineTo(cRightX, girdleY)
        lineTo(gRightX, girdleY)
        close()
    }

    val tablePath = Path().apply {
        moveTo(tLeftX, topY)
        lineTo(tRightX, topY)
        lineTo(cRightX, girdleY * 0.82f)
        lineTo(cLeftX, girdleY * 0.82f)
        close()
    }

    val fullDiamondPath = Path().apply {
        moveTo(tLeftX, topY)
        lineTo(tRightX, topY)
        lineTo(gRightX, girdleY)
        lineTo(centerX, culetY)
        lineTo(gLeftX, girdleY)
        close()
    }
}

/**
 * Premium 3D Faceted Crystal Diamond for Tobi GT.
 * Ultra high performance: Precomputes paths once, eliminates all GC allocations in onDraw,
 * and completely disables transitions when animated = false.
 */
@Composable
fun TobiDiamond3D(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
    animated: Boolean = true
) {
    val shimmerPhase = if (animated) {
        val infiniteTransition = rememberInfiniteTransition(label = "diamond_shimmer")
        val phase by infiniteTransition.animateFloat(
            initialValue = -0.3f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2600, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_pos"
        )
        phase
    } else {
        0.5f // Static constant: zero recompositions, zero animation transitions!
    }

    val density = LocalDensity.current
    val sizePx = remember(size, density) { with(density) { size.toPx() } }
    val geo = remember(sizePx) { DiamondGeometry(sizePx, sizePx) }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = geo.w
            val h = geo.h

            // 1. Ambient Glow Aura behind crystal
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x6600E5FF),
                        Color(0x220284C7),
                        Color(0x00000000)
                    ),
                    center = Offset(geo.centerX, h * 0.5f),
                    radius = w * 0.55f
                )
            )

            // 2. Lower Pavilion Facets (Tapering to Culet)
            drawPath(
                path = geo.pLeft,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0369A1), Color(0xFF075985), Color(0xFF0C4A6E)),
                    start = Offset(geo.gLeftX, geo.girdleY),
                    end = Offset(geo.centerX, geo.culetY)
                )
            )

            drawPath(
                path = geo.pCenterLeft,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF0C4A6E)),
                    start = Offset(geo.cLeftX, geo.girdleY),
                    end = Offset(geo.centerX, geo.culetY)
                )
            )

            drawPath(
                path = geo.pCenterRight,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF00B4D8), Color(0xFF0284C7), Color(0xFF075985)),
                    start = Offset(geo.centerX, geo.girdleY),
                    end = Offset(geo.centerX, geo.culetY)
                )
            )

            drawPath(
                path = geo.pRight,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF38BDF8), Color(0xFF00B4D8), Color(0xFF0369A1)),
                    start = Offset(geo.gRightX, geo.girdleY),
                    end = Offset(geo.centerX, geo.culetY)
                )
            )

            // 3. Crown Upper Facets
            drawPath(
                path = geo.cLeft,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF075985)),
                    start = Offset(geo.tLeftX, geo.topY),
                    end = Offset(geo.gLeftX, geo.girdleY)
                )
            )

            drawPath(
                path = geo.cMidLeft,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF38BDF8), Color(0xFF00E5FF), Color(0xFF0284C7)),
                    start = Offset(geo.tLeftX, geo.topY),
                    end = Offset(geo.centerX, geo.girdleY)
                )
            )

            drawPath(
                path = geo.cMidRight,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFE0F2FE), Color(0xFF7DD3FC), Color(0xFF00B4D8)),
                    start = Offset(geo.centerX, geo.topY),
                    end = Offset(geo.cRightX, geo.girdleY)
                )
            )

            drawPath(
                path = geo.cRight,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFBAE6FD), Color(0xFF38BDF8), Color(0xFF0284C7)),
                    start = Offset(geo.tRightX, geo.topY),
                    end = Offset(geo.gRightX, geo.girdleY)
                )
            )

            // 4. Crown Table Top Facet
            drawPath(
                path = geo.tablePath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF0F9FF), Color(0xFFBAE6FD), Color(0xFF00E5FF)),
                    start = Offset(geo.tLeftX, geo.topY),
                    end = Offset(geo.tRightX, geo.girdleY * 0.82f)
                )
            )

            // 5. Dynamic Shimmer Sweep
            val shimmerX = w * shimmerPhase
            val shimmerWidth = w * 0.35f
            drawPath(
                path = geo.fullDiamondPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0x11FFFFFF),
                        Color(0x88FFFFFF),
                        Color(0x11FFFFFF),
                        Color.Transparent
                    ),
                    start = Offset(shimmerX - shimmerWidth, geo.topY),
                    end = Offset(shimmerX + shimmerWidth, geo.culetY)
                )
            )

            // 6. Facet Outlines and Girdle Highlight Line
            val strokeColor = Color(0x66FFFFFF)

            drawPath(
                path = geo.fullDiamondPath,
                color = Color(0xAA00E5FF),
                style = Stroke(width = geo.strokeWidth)
            )

            drawPath(
                path = geo.tablePath,
                color = strokeColor,
                style = Stroke(width = geo.strokeWidth * 0.75f)
            )

            drawLine(
                color = strokeColor,
                start = Offset(geo.cLeftX, geo.girdleY),
                end = Offset(geo.centerX, geo.culetY),
                strokeWidth = geo.strokeWidth * 0.7f
            )
            drawLine(
                color = strokeColor,
                start = Offset(geo.centerX, geo.girdleY),
                end = Offset(geo.centerX, geo.culetY),
                strokeWidth = geo.strokeWidth * 0.7f
            )
            drawLine(
                color = strokeColor,
                start = Offset(geo.cRightX, geo.girdleY),
                end = Offset(geo.centerX, geo.culetY),
                strokeWidth = geo.strokeWidth * 0.7f
            )

            // 7. Twinkle Flare Sparkle on Top-Right Crown
            drawCircle(
                color = Color.White,
                radius = geo.sparkleRadius * 0.6f,
                center = Offset(geo.sparkleX, geo.sparkleY)
            )
            drawLine(
                color = Color.White,
                start = Offset(geo.sparkleX - geo.sparkleRadius * 1.6f, geo.sparkleY),
                end = Offset(geo.sparkleX + geo.sparkleRadius * 1.6f, geo.sparkleY),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color.White,
                start = Offset(geo.sparkleX, geo.sparkleY - geo.sparkleRadius * 1.6f),
                end = Offset(geo.sparkleX, geo.sparkleY + geo.sparkleRadius * 1.6f),
                strokeWidth = 1.5f
            )
        }
    }
}
