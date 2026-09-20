package com.example.game

import androidx.compose.ui.graphics.Color

enum class GameStatus {
    PLAYING,
    PAUSED,
    GAME_OVER
}

enum class EnemyType {
    SCOUT,
    FIGHTER,
    BOMBER,
    ASTEROID,
    BOSS
}

enum class PowerUpType {
    TRIPLE_SHOT,
    SHIELD,
    SPEED_BOOST,
    EMP_BOMB
}

data class PlayerState(
    var x: Float = 0f,
    var y: Float = 0f,
    val width: Float = 60f,
    val height: Float = 70f,
    var lives: Int = 3,
    val maxLives: Int = 3,
    var hasShield: Boolean = false,
    var tripleShotTicks: Int = 0,
    var speedBoostTicks: Int = 0,
    var invulnerableTicks: Int = 0
)

data class Laser(
    val id: Long,
    var x: Float,
    var y: Float,
    val vx: Float = 0f,
    val vy: Float,
    val isEnemy: Boolean = false,
    val radius: Float = 5f,
    val color: Color
)

data class Enemy(
    val id: Long,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val width: Float,
    val height: Float,
    val type: EnemyType,
    var hp: Int,
    val maxHp: Int,
    val scoreValue: Int,
    val coinDropChance: Float,
    var fireCooldown: Int = 0
)

data class PowerUp(
    val id: Long,
    var x: Float,
    var y: Float,
    val vy: Float = 2.5f,
    val type: PowerUpType,
    val radius: Float = 18f
)

data class CoinDrop(
    val id: Long,
    var x: Float,
    var y: Float,
    var vy: Float = 2.2f,
    val value: Int = 1,
    val radius: Float = 14f
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    var life: Float,
    val maxLife: Float,
    val radius: Float
)
