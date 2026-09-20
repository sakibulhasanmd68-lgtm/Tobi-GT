package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.EnemyType
import com.example.game.GameAudio
import com.example.game.GameEngine
import com.example.game.GameStatus
import com.example.game.PowerUpType
import com.example.ui.components.CyberButton
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCrimson
import com.example.ui.theme.NeonGold
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceTextMuted
import com.example.ui.theme.SpaceTextPrimary
import com.example.ui.theme.SpaceTextSecondary

private class PrecomputedStar(
    val xFrac: Float,
    val yOffsetFrac: Float,
    val speedFactor: Float,
    val radius: Float,
    val alpha: Float
)

@Composable
fun GameScreen(
    gameAudio: GameAudio,
    highScore: Int,
    onGameFinished: (score: Int, enemiesKilled: Int, bossesKilled: Int, coinsCollected: Int) -> Unit,
    onExitHome: () -> Unit
) {
    var frameTick by remember { mutableLongStateOf(0L) }

    val engine = remember {
        GameEngine(
            audio = gameAudio,
            onGameFinished = { score, enemies, bosses, coins ->
                onGameFinished(score, enemies, bosses, coins)
            }
        )
    }

    // 60 FPS Game Loop
    LaunchedEffect(engine) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                engine.update()
                frameTick = frameTimeNanos
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .testTag("game_screen")
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        LaunchedEffect(widthPx, heightPx) {
            engine.initGame(widthPx, heightPx)
        }

        // Isolated high-performance Canvas view with zero heap allocations per frame
        GameCanvasRenderer(
            engine = engine,
            frameTick = frameTick,
            widthPx = widthPx,
            heightPx = heightPx,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        engine.movePlayer(
                            engine.player.x + dragAmount.x * 1.1f,
                            engine.player.y + dragAmount.y * 1.1f
                        )
                    }
                }
        )

        // ==================== HUD OVERLAYS ====================

        // Top Status Bar: Score, Diamonds, Lives, Pause
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score & High Score
                Column {
                    Text(
                        text = "SCORE: %,d".format(engine.score),
                        color = SpaceTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "HI: %,d".format(maxOf(highScore, engine.score)),
                        color = SpaceTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Diamonds Collected
                Surface(
                    color = SpaceCardBg.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💎", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${engine.coinsCollected}",
                            color = Color(0xFF0284C7),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Lives Indicator & Pause Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(3) { index ->
                        val isAlive = index < engine.player.lives
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = if (isAlive) NeonCrimson else Color(0xFF374151),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(horizontal = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = { engine.togglePause() },
                        modifier = Modifier
                            .testTag("game_pause_btn")
                            .size(36.dp)
                            .background(SpaceCardBg.copy(alpha = 0.85f), CircleShape)
                            .border(1.dp, CyberCyan.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause",
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Boss Health Bar (when Boss is active)
            val boss = engine.currentBoss
            if (boss != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = SpaceCardBg.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCrimson.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "BOSS: TITAN GT-01",
                                color = NeonCrimson,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${boss.hp}/${boss.maxHp}",
                                color = SpaceTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        val progress = (boss.hp.toFloat() / boss.maxHp.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = NeonCrimson,
                            trackColor = Color(0xFF374151)
                        )
                    }
                }
            }

            // Boss Approach Siren Banner
            if (engine.bossAlertTicks > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = NeonCrimson.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WARNING: TITAN GT-01 DREADNOUGHT APPROACHING",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        // Bottom EMP Bomb Emergency Trigger
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                color = NeonAmber.copy(alpha = 0.9f),
                shape = CircleShape,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .testTag("game_bomb_btn")
                    .pointerInput(Unit) {
                        detectDragGestures { _, _ -> }
                    }
            ) {
                IconButton(onClick = { engine.triggerBomb() }) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "EMP Bomb",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // ==================== PAUSE MODAL ====================
        AnimatedVisibility(
            visible = engine.status == GameStatus.PAUSED,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .testTag("game_pause_overlay"),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberCyan),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BATTLE PAUSED",
                            color = CyberCyan,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        CyberButton(
                            text = "RESUME FLIGHT",
                            onClick = { engine.togglePause() },
                            icon = Icons.Default.PlayArrow,
                            isPrimary = true,
                            testTag = "game_resume_btn"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CyberButton(
                            text = "RESTART SORTIE",
                            onClick = { engine.resetGame() },
                            icon = Icons.Default.Refresh,
                            isPrimary = false,
                            testTag = "game_restart_btn"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CyberButton(
                            text = "ABORT TO HOME",
                            onClick = onExitHome,
                            isPrimary = false,
                            testTag = "game_abort_btn"
                        )
                    }
                }
            }
        }

        // ==================== GAME OVER MODAL ====================
        AnimatedVisibility(
            visible = engine.status == GameStatus.GAME_OVER,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .testTag("game_over_overlay"),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = SpaceCardBg,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonCrimson),
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "MISSION FAILED",
                            color = NeonCrimson,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Results Box
                        Surface(
                            color = SpaceDarkBg,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SpaceCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                ResultStatRow("FINAL SCORE", "%,d".format(engine.score), DiamondCyan)
                                ResultStatRow("DIAMONDS COLLECTED", "+${engine.coinsCollected}", DiamondCyan)
                                ResultStatRow("ENEMIES DESTROYED", "${engine.enemiesKilled}", SpaceTextPrimary)
                                ResultStatRow("BOSSES DEFEATED", "${engine.bossesKilled}", NeonCrimson)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        CyberButton(
                            text = "RE-DEPLOY (PLAY AGAIN)",
                            onClick = { engine.resetGame() },
                            icon = Icons.Default.Refresh,
                            isPrimary = true,
                            testTag = "game_over_retry_btn"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CyberButton(
                            text = "RETURN TO HOME",
                            onClick = onExitHome,
                            isPrimary = false,
                            testTag = "game_over_home_btn"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Highly optimized Canvas renderer. Reuses Path buffers to avoid garbage collection pauses,
 * uses direct rotation transforms for diamond rendering, and avoids Color.copy allocations.
 */
@Composable
private fun GameCanvasRenderer(
    engine: GameEngine,
    frameTick: Long,
    widthPx: Float,
    heightPx: Float,
    modifier: Modifier = Modifier
) {
    val reusablePath1 = remember { Path() }
    val reusablePath2 = remember { Path() }

    val precomputedStars = remember {
        val stars = mutableListOf<PrecomputedStar>()
        for (i in 0 until 35) {
            val seed = i * 137L
            val xFrac = (seed * 97L % 1000) / 1000f
            val yOffsetFrac = (seed * 73L % 1000) / 1000f
            val speedFactor = (1 + (i % 3)) * 0.0000000625f
            val radius = if (i % 3 == 0) 2.2f else 1.2f
            val alpha = if (i % 2 == 0) 0.8f else 0.4f
            stars.add(PrecomputedStar(xFrac, yOffsetFrac, speedFactor, radius, alpha))
        }
        stars
    }

    Canvas(modifier = modifier) {
        val tick = frameTick // Triggers redraw of this canvas only

        // 1. Scrolling Starfield
        for (star in precomputedStars) {
            val starX = star.xFrac * widthPx
            val starY = ((star.yOffsetFrac * heightPx) + (tick * star.speedFactor * heightPx)) % heightPx
            drawCircle(
                color = Color.White,
                alpha = star.alpha,
                radius = star.radius,
                center = Offset(starX, starY)
            )
        }

        // 2. Render PowerUps
        for (pu in engine.powerUps) {
            val puColor = when (pu.type) {
                PowerUpType.TRIPLE_SHOT -> CyberCyan
                PowerUpType.SHIELD -> Color(0xFF38BDF8)
                PowerUpType.SPEED_BOOST -> NeonGold
                PowerUpType.EMP_BOMB -> NeonCrimson
            }
            drawCircle(color = puColor, alpha = 0.3f, radius = pu.radius * 1.4f, center = Offset(pu.x, pu.y))
            drawCircle(color = puColor, radius = pu.radius, center = Offset(pu.x, pu.y))
            drawCircle(color = Color.White, radius = pu.radius * 0.4f, center = Offset(pu.x, pu.y))
        }

        // 3. Render Collectible Diamonds (Direct rotation transform, ZERO Path allocations)
        for (coin in engine.coins) {
            val dSize = coin.radius * 0.9f
            rotate(45f, pivot = Offset(coin.x, coin.y)) {
                drawRect(
                    color = Color(0xFF0284C7),
                    topLeft = Offset(coin.x - dSize, coin.y - dSize),
                    size = Size(dSize * 2f, dSize * 2f)
                )
                val innerSize = dSize * 0.55f
                drawRect(
                    color = Color(0xFF38BDF8),
                    topLeft = Offset(coin.x - innerSize, coin.y - innerSize),
                    size = Size(innerSize * 2f, innerSize * 2f)
                )
            }
        }

        // 4. Render Lasers
        for (laser in engine.lasers) {
            drawCircle(
                color = laser.color,
                radius = laser.radius,
                center = Offset(laser.x, laser.y)
            )
            val trailLen = if (laser.isEnemy) 12f else 18f
            drawLine(
                color = laser.color,
                alpha = 0.6f,
                start = Offset(laser.x, laser.y),
                end = Offset(laser.x - laser.vx * 0.5f, laser.y - laser.vy * 0.8f),
                strokeWidth = laser.radius * 1.5f
            )
        }

        // 5. Render Enemies & Boss (using reusablePath to eliminate GC)
        for (enemy in engine.enemies) {
            when (enemy.type) {
                EnemyType.SCOUT -> {
                    reusablePath1.reset()
                    reusablePath1.moveTo(enemy.x, enemy.y + enemy.height / 2f)
                    reusablePath1.lineTo(enemy.x - enemy.width / 2f, enemy.y)
                    reusablePath1.lineTo(enemy.x, enemy.y - enemy.height / 2f)
                    reusablePath1.lineTo(enemy.x + enemy.width / 2f, enemy.y)
                    reusablePath1.close()
                    drawPath(reusablePath1, color = Color(0xFFF97316))
                    drawCircle(color = Color(0xFFFFE4E6), radius = 4f, center = Offset(enemy.x, enemy.y))
                }
                EnemyType.FIGHTER -> {
                    reusablePath1.reset()
                    reusablePath1.moveTo(enemy.x, enemy.y + enemy.height / 2f)
                    reusablePath1.lineTo(enemy.x + enemy.width / 2f, enemy.y - enemy.height / 2f)
                    reusablePath1.lineTo(enemy.x, enemy.y - enemy.height / 4f)
                    reusablePath1.lineTo(enemy.x - enemy.width / 2f, enemy.y - enemy.height / 2f)
                    reusablePath1.close()
                    drawPath(reusablePath1, color = NeonCrimson)
                    drawCircle(color = NeonAmber, radius = 5f, center = Offset(enemy.x, enemy.y - 4f))
                }
                EnemyType.BOMBER -> {
                    drawRoundRect(
                        color = Color(0xFF7C3AED),
                        topLeft = Offset(enemy.x - enemy.width / 2f, enemy.y - enemy.height / 2f),
                        size = Size(enemy.width, enemy.height),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                    drawCircle(color = Color(0xFFC084FC), radius = 10f, center = Offset(enemy.x, enemy.y))
                }
                EnemyType.ASTEROID -> {
                    drawCircle(
                        color = Color(0xFF64748B),
                        radius = enemy.width / 2f,
                        center = Offset(enemy.x, enemy.y)
                    )
                    drawCircle(
                        color = Color(0xFF94A3B8),
                        radius = enemy.width * 0.3f,
                        center = Offset(enemy.x - 6f, enemy.y - 6f)
                    )
                }
                EnemyType.BOSS -> {
                    val bw = enemy.width
                    val bh = enemy.height
                    reusablePath1.reset()
                    reusablePath1.moveTo(enemy.x, enemy.y + bh * 0.45f)
                    reusablePath1.lineTo(enemy.x + bw * 0.45f, enemy.y + bh * 0.15f)
                    reusablePath1.lineTo(enemy.x + bw * 0.5f, enemy.y - bh * 0.35f)
                    reusablePath1.lineTo(enemy.x + bw * 0.25f, enemy.y - bh * 0.5f)
                    reusablePath1.lineTo(enemy.x, enemy.y - bh * 0.3f)
                    reusablePath1.lineTo(enemy.x - bw * 0.25f, enemy.y - bh * 0.5f)
                    reusablePath1.lineTo(enemy.x - bw * 0.5f, enemy.y - bh * 0.35f)
                    reusablePath1.lineTo(enemy.x - bw * 0.45f, enemy.y + bh * 0.15f)
                    reusablePath1.close()

                    drawPath(reusablePath1, color = Color(0xFF4C0519))
                    drawPath(reusablePath1, color = NeonCrimson, style = Stroke(width = 3.5f))
                    drawCircle(color = NeonAmber, radius = 22f, center = Offset(enemy.x, enemy.y))
                    drawCircle(color = Color.White, radius = 10f, center = Offset(enemy.x, enemy.y))
                }
            }
        }

        // 6. Render Particles
        for (particle in engine.particles) {
            val alpha = (particle.life / particle.maxLife).coerceIn(0f, 1f)
            drawCircle(
                color = particle.color,
                alpha = alpha,
                radius = particle.radius,
                center = Offset(particle.x, particle.y)
            )
        }

        // 7. Render Player Jet (using reusable paths)
        val p = engine.player
        val isBlinking = p.invulnerableTicks > 0 && (p.invulnerableTicks / 4) % 2 == 0
        if (!isBlinking) {
            val pw = p.width
            val ph = p.height

            // Jet Thruster Flame
            reusablePath1.reset()
            reusablePath1.moveTo(p.x - pw * 0.15f, p.y + ph * 0.4f)
            reusablePath1.lineTo(p.x, p.y + ph * 0.75f + (tick % 5))
            reusablePath1.lineTo(p.x + pw * 0.15f, p.y + ph * 0.4f)
            reusablePath1.close()
            drawPath(reusablePath1, color = Color(0xFF38BDF8))

            // Jet Wings & Fuselage
            reusablePath1.reset()
            reusablePath1.moveTo(p.x, p.y - ph * 0.5f)
            reusablePath1.lineTo(p.x + pw * 0.15f, p.y - ph * 0.15f)
            reusablePath1.lineTo(p.x + pw * 0.5f, p.y + ph * 0.25f)
            reusablePath1.lineTo(p.x + pw * 0.2f, p.y + ph * 0.35f)
            reusablePath1.lineTo(p.x, p.y + ph * 0.2f)
            reusablePath1.lineTo(p.x - pw * 0.2f, p.y + ph * 0.35f)
            reusablePath1.lineTo(p.x - pw * 0.5f, p.y + ph * 0.25f)
            reusablePath1.lineTo(p.x - pw * 0.15f, p.y - ph * 0.15f)
            reusablePath1.close()
            drawPath(reusablePath1, color = CyberCyan)

            // Cockpit Core
            reusablePath2.reset()
            reusablePath2.moveTo(p.x, p.y - ph * 0.3f)
            reusablePath2.lineTo(p.x + pw * 0.08f, p.y - ph * 0.05f)
            reusablePath2.lineTo(p.x, p.y + ph * 0.05f)
            reusablePath2.lineTo(p.x - pw * 0.08f, p.y - ph * 0.05f)
            reusablePath2.close()
            drawPath(reusablePath2, color = NeonGold)

            // Shield Energy Bubble
            if (p.hasShield) {
                drawCircle(
                    color = Color(0xFF00F0FF),
                    alpha = 0.3f,
                    radius = pw * 0.8f,
                    center = Offset(p.x, p.y)
                )
                drawCircle(
                    color = Color(0xFF00F0FF),
                    radius = pw * 0.8f,
                    center = Offset(p.x, p.y),
                    style = Stroke(width = 2.5f)
                )
            }
        }
    }
}

@Composable
private fun ResultStatRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = SpaceTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
