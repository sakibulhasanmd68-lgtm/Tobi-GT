package com.example.game

import androidx.compose.ui.graphics.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

class GameEngine(
    val audio: GameAudio,
    val onGameFinished: (score: Int, enemiesKilled: Int, bossesKilled: Int, coinsCollected: Int) -> Unit
) {
    var screenWidth: Float = 1080f
    var screenHeight: Float = 1920f

    var status: GameStatus = GameStatus.PLAYING
    val player = PlayerState()

    val lasers = mutableListOf<Laser>()
    val enemies = mutableListOf<Enemy>()
    val powerUps = mutableListOf<PowerUp>()
    val coins = mutableListOf<CoinDrop>()
    val particles = mutableListOf<Particle>()

    var score: Int = 0
    var coinsCollected: Int = 0
    var enemiesKilled: Int = 0
    var bossesKilled: Int = 0
    var waveNumber: Int = 1

    private var nextEntityId = 1L
    private var tickCount = 0
    private var shootCooldown = 0
    private var nextBossScore = 2000
    var currentBoss: Enemy? = null
    var bossAlertTicks: Int = 0

    fun initGame(width: Float, height: Float) {
        screenWidth = width
        screenHeight = height
        resetGame()
    }

    fun resetGame() {
        status = GameStatus.PLAYING
        player.x = screenWidth / 2f
        player.y = screenHeight * 0.8f
        player.lives = 3
        player.hasShield = false
        player.tripleShotTicks = 0
        player.speedBoostTicks = 0
        player.invulnerableTicks = 60

        lasers.clear()
        enemies.clear()
        powerUps.clear()
        coins.clear()
        particles.clear()

        score = 0
        coinsCollected = 0
        enemiesKilled = 0
        bossesKilled = 0
        waveNumber = 1
        shootCooldown = 0
        tickCount = 0
        nextBossScore = 2000
        currentBoss = null
        bossAlertTicks = 0
    }

    fun movePlayer(targetX: Float, targetY: Float) {
        if (status != GameStatus.PLAYING) return
        val margin = 40f
        player.x = targetX.coerceIn(margin, screenWidth - margin)
        player.y = targetY.coerceIn(screenHeight * 0.2f, screenHeight - margin)
    }

    fun togglePause() {
        if (status == GameStatus.PLAYING) {
            status = GameStatus.PAUSED
        } else if (status == GameStatus.PAUSED) {
            status = GameStatus.PLAYING
        }
    }

    fun triggerBomb() {
        if (status != GameStatus.PLAYING) return
        audio.playExplosionSound()
        // Destroy non-boss enemies and bullets
        val enemiesIterator = enemies.iterator()
        while (enemiesIterator.hasNext()) {
            val enemy = enemiesIterator.next()
            if (enemy.type == EnemyType.BOSS) {
                enemy.hp = (enemy.hp - 35).coerceAtLeast(1)
            } else {
                spawnExplosion(enemy.x, enemy.y, Color(0xFFFF4444), 15)
                score += enemy.scoreValue
                enemiesKilled++
                spawnCoin(enemy.x, enemy.y, 2)
                enemiesIterator.remove()
            }
        }
        // Clear enemy lasers
        lasers.removeAll { it.isEnemy }
        spawnExplosion(screenWidth / 2f, screenHeight / 2f, Color(0xFF00F0FF), 30)
    }

    fun update() {
        if (status != GameStatus.PLAYING) return
        tickCount++

        // Decrement timers
        if (player.tripleShotTicks > 0) player.tripleShotTicks--
        if (player.speedBoostTicks > 0) player.speedBoostTicks--
        if (player.invulnerableTicks > 0) player.invulnerableTicks--
        if (bossAlertTicks > 0) bossAlertTicks--

        // Player Auto-Shooting
        if (shootCooldown > 0) {
            shootCooldown--
        } else {
            firePlayerLaser()
            shootCooldown = if (player.speedBoostTicks > 0) 7 else 11
        }

        // Spawn Enemies & Bosses
        handleEnemySpawning()

        // Update Lasers
        updateLasers()

        // Update Enemies
        updateEnemies()

        // Update PowerUps & Coins
        updatePickups()

        // Update Particles
        updateParticles()

        // Collision Checks
        handleCollisions()

        // Check Boss Defeat / State
        if (currentBoss != null && !enemies.contains(currentBoss)) {
            currentBoss = null
        }
    }

    private fun firePlayerLaser() {
        audio.playLaserSound()
        val isTriple = player.tripleShotTicks > 0
        val py = player.y - player.height / 2f

        if (isTriple) {
            lasers.add(Laser(nextEntityId++, player.x, py, vx = 0f, vy = -22f, color = Color(0xFF00F0FF)))
            lasers.add(Laser(nextEntityId++, player.x - 18f, py + 10f, vx = -4f, vy = -20f, color = Color(0xFF38BDF8)))
            lasers.add(Laser(nextEntityId++, player.x + 18f, py + 10f, vx = 4f, vy = -20f, color = Color(0xFF38BDF8)))
        } else {
            lasers.add(Laser(nextEntityId++, player.x - 12f, py, vx = 0f, vy = -22f, color = Color(0xFF00F0FF)))
            lasers.add(Laser(nextEntityId++, player.x + 12f, py, vx = 0f, vy = -22f, color = Color(0xFF00F0FF)))
        }
    }

    private fun handleEnemySpawning() {
        // Check for Boss Spawn
        if (score >= nextBossScore && currentBoss == null) {
            nextBossScore += 3500
            spawnBoss()
            return
        }

        // Regular enemy spawning rate increases with score
        val spawnInterval = (70 - (score / 400).coerceAtMost(40)).coerceAtLeast(25)
        if (tickCount % spawnInterval == 0 && (currentBoss == null || enemies.size < 4)) {
            val roll = Random.nextFloat()
            val spawnX = Random.nextFloat() * (screenWidth - 120f) + 60f

            when {
                roll < 0.45f -> {
                    // Scout drone: fast, agile
                    enemies.add(
                        Enemy(
                            id = nextEntityId++,
                            x = spawnX,
                            y = -50f,
                            vx = (Random.nextFloat() - 0.5f) * 3f,
                            vy = Random.nextFloat() * 2f + 4f,
                            width = 46f,
                            height = 46f,
                            type = EnemyType.SCOUT,
                            hp = 2,
                            maxHp = 2,
                            scoreValue = 80,
                            coinDropChance = 0.4f
                        )
                    )
                }
                roll < 0.75f -> {
                    // Fighter Jet: shoots back
                    enemies.add(
                        Enemy(
                            id = nextEntityId++,
                            x = spawnX,
                            y = -60f,
                            vx = (Random.nextFloat() - 0.5f) * 2f,
                            vy = Random.nextFloat() * 1.5f + 3f,
                            width = 56f,
                            height = 60f,
                            type = EnemyType.FIGHTER,
                            hp = 4,
                            maxHp = 4,
                            scoreValue = 150,
                            coinDropChance = 0.6f,
                            fireCooldown = Random.nextInt(30, 60)
                        )
                    )
                }
                roll < 0.90f -> {
                    // Bomber Gunship: tanky, high coin drop
                    enemies.add(
                        Enemy(
                            id = nextEntityId++,
                            x = spawnX,
                            y = -80f,
                            vx = (Random.nextFloat() - 0.5f) * 1.2f,
                            vy = 1.8f,
                            width = 76f,
                            height = 80f,
                            type = EnemyType.BOMBER,
                            hp = 9,
                            maxHp = 9,
                            scoreValue = 300,
                            coinDropChance = 0.85f,
                            fireCooldown = Random.nextInt(20, 50)
                        )
                    )
                }
                else -> {
                    // Plasma Asteroid: obstacle
                    enemies.add(
                        Enemy(
                            id = nextEntityId++,
                            x = spawnX,
                            y = -60f,
                            vx = (Random.nextFloat() - 0.5f) * 1.5f,
                            vy = Random.nextFloat() * 2f + 3.5f,
                            width = 52f,
                            height = 52f,
                            type = EnemyType.ASTEROID,
                            hp = 6,
                            maxHp = 6,
                            scoreValue = 100,
                            coinDropChance = 0.5f
                        )
                    )
                }
            }
        }
    }

    private fun spawnBoss() {
        bossAlertTicks = 120
        audio.playBossAlarmSound()
        val boss = Enemy(
            id = nextEntityId++,
            x = screenWidth / 2f,
            y = -120f,
            vx = 2.2f,
            vy = 1.2f,
            width = 160f,
            height = 140f,
            type = EnemyType.BOSS,
            hp = 120 + (bossesKilled * 35),
            maxHp = 120 + (bossesKilled * 35),
            scoreValue = 1500,
            coinDropChance = 1.0f,
            fireCooldown = 30
        )
        currentBoss = boss
        enemies.add(boss)
    }

    private fun updateLasers() {
        val laserIterator = lasers.iterator()
        while (laserIterator.hasNext()) {
            val laser = laserIterator.next()
            laser.x += laser.vx
            laser.y += laser.vy

            if (laser.y < -30f || laser.y > screenHeight + 30f || laser.x < -30f || laser.x > screenWidth + 30f) {
                laserIterator.remove()
            }
        }
    }

    private fun updateEnemies() {
        val enemyIterator = enemies.iterator()
        while (enemyIterator.hasNext()) {
            val enemy = enemyIterator.next()

            if (enemy.type == EnemyType.BOSS) {
                // Boss enters to top 20% and patrols horizontally
                if (enemy.y < screenHeight * 0.18f) {
                    enemy.y += enemy.vy
                } else {
                    enemy.x += enemy.vx
                    if (enemy.x <= enemy.width / 2f + 20f || enemy.x >= screenWidth - enemy.width / 2f - 20f) {
                        enemy.vx = -enemy.vx
                    }
                }

                // Boss firing barrage
                enemy.fireCooldown--
                if (enemy.fireCooldown <= 0) {
                    enemy.fireCooldown = 35
                    // Triple enemy laser spread
                    lasers.add(Laser(nextEntityId++, enemy.x - 40f, enemy.y + 40f, vx = -2f, vy = 9f, isEnemy = true, color = Color(0xFFFF2255)))
                    lasers.add(Laser(nextEntityId++, enemy.x, enemy.y + 50f, vx = 0f, vy = 11f, isEnemy = true, color = Color(0xFFFF0033)))
                    lasers.add(Laser(nextEntityId++, enemy.x + 40f, enemy.y + 40f, vx = 2f, vy = 9f, isEnemy = true, color = Color(0xFFFF2255)))
                }
            } else {
                enemy.x += enemy.vx
                enemy.y += enemy.vy

                // Bounce horizontally off screen walls
                if (enemy.x < enemy.width / 2f || enemy.x > screenWidth - enemy.width / 2f) {
                    enemy.vx = -enemy.vx
                }

                // Enemy shooting
                if (enemy.type == EnemyType.FIGHTER || enemy.type == EnemyType.BOMBER) {
                    enemy.fireCooldown--
                    if (enemy.fireCooldown <= 0) {
                        enemy.fireCooldown = Random.nextInt(60, 110)
                        lasers.add(
                            Laser(
                                id = nextEntityId++,
                                x = enemy.x,
                                y = enemy.y + enemy.height / 2f,
                                vx = (player.x - enemy.x).coerceIn(-4f, 4f) * 0.15f,
                                vy = 8f,
                                isEnemy = true,
                                color = Color(0xFFFF3366)
                            )
                        )
                    }
                }

                if (enemy.y > screenHeight + enemy.height) {
                    enemyIterator.remove()
                }
            }
        }
    }

    private fun updatePickups() {
        // PowerUps fall down
        val puIterator = powerUps.iterator()
        while (puIterator.hasNext()) {
            val pu = puIterator.next()
            pu.y += pu.vy

            // Player pickup
            if (hypot(player.x - pu.x, player.y - pu.y) < pu.radius + 30f) {
                applyPowerUp(pu.type)
                audio.playPowerUpSound()
                spawnExplosion(pu.x, pu.y, Color(0xFF00FFCC), 12)
                puIterator.remove()
            } else if (pu.y > screenHeight + 50f) {
                puIterator.remove()
            }
        }

        // Coins fall and magnetize towards player
        val coinIterator = coins.iterator()
        while (coinIterator.hasNext()) {
            val coin = coinIterator.next()
            val distToPlayer = hypot(player.x - coin.x, player.y - coin.y)

            // Magnetic attraction within 160 units
            if (distToPlayer < 180f) {
                val angle = atan2(player.y - coin.y, player.x - coin.x)
                coin.x += cos(angle) * 8f
                coin.y += sin(angle) * 8f
            } else {
                coin.y += coin.vy
            }

            // Collection
            if (distToPlayer < coin.radius + 32f) {
                coinsCollected += coin.value
                score += 25
                audio.playCoinSound()
                spawnExplosion(coin.x, coin.y, Color(0xFFFFD700), 8)
                coinIterator.remove()
            } else if (coin.y > screenHeight + 50f) {
                coinIterator.remove()
            }
        }
    }

    private fun updateParticles() {
        val pIterator = particles.iterator()
        while (pIterator.hasNext()) {
            val p = pIterator.next()
            p.x += p.vx
            p.y += p.vy
            p.life -= 1f
            if (p.life <= 0f) {
                pIterator.remove()
            }
        }
    }

    private fun handleCollisions() {
        // 1. Player lasers vs Enemies
        val laserIterator = lasers.iterator()
        while (laserIterator.hasNext()) {
            val laser = laserIterator.next()
            if (laser.isEnemy) continue

            for (enemy in enemies) {
                if (isColliding(laser.x, laser.y, laser.radius * 2f, laser.radius * 2f, enemy.x, enemy.y, enemy.width, enemy.height)) {
                    enemy.hp--
                    spawnExplosion(laser.x, laser.y, Color(0xFF00F0FF), 5)
                    laserIterator.remove()

                    if (enemy.hp <= 0) {
                        onEnemyDestroyed(enemy)
                        enemies.remove(enemy)
                    }
                    break
                }
            }
        }

        // 2. Enemy / Enemy Lasers vs Player
        if (player.invulnerableTicks <= 0) {
            // Check enemy bullets
            val enemyLaserIterator = lasers.iterator()
            while (enemyLaserIterator.hasNext()) {
                val laser = enemyLaserIterator.next()
                if (laser.isEnemy) {
                    if (hypot(player.x - laser.x, player.y - laser.y) < 28f) {
                        enemyLaserIterator.remove()
                        damagePlayer()
                        break
                    }
                }
            }

            // Check enemy ship collision
            for (enemy in enemies) {
                if (isColliding(player.x, player.y, player.width * 0.7f, player.height * 0.7f, enemy.x, enemy.y, enemy.width, enemy.height)) {
                    damagePlayer()
                    if (enemy.type != EnemyType.BOSS) {
                        enemy.hp = 0
                        onEnemyDestroyed(enemy)
                        enemies.remove(enemy)
                    }
                    break
                }
            }
        }
    }

    private fun damagePlayer() {
        if (player.hasShield) {
            player.hasShield = false
            player.invulnerableTicks = 35
            audio.playExplosionSound()
            spawnExplosion(player.x, player.y, Color(0xFF38BDF8), 16)
            return
        }

        player.lives--
        player.invulnerableTicks = 60
        audio.playExplosionSound()
        spawnExplosion(player.x, player.y, Color(0xFFFF3333), 25)

        if (player.lives <= 0) {
            status = GameStatus.GAME_OVER
            onGameFinished(score, enemiesKilled, bossesKilled, coinsCollected)
        }
    }

    private fun onEnemyDestroyed(enemy: Enemy) {
        audio.playExplosionSound()
        enemiesKilled++
        score += enemy.scoreValue

        if (enemy.type == EnemyType.BOSS) {
            bossesKilled++
            spawnExplosion(enemy.x, enemy.y, Color(0xFFFFCC00), 50)
            spawnExplosion(enemy.x - 30f, enemy.y + 20f, Color(0xFFFF3300), 30)
            spawnExplosion(enemy.x + 30f, enemy.y - 20f, Color(0xFF00F0FF), 30)
            // Huge coin shower
            for (i in 0 until 12) {
                spawnCoin(enemy.x + (Random.nextFloat() - 0.5f) * 120f, enemy.y + (Random.nextFloat() - 0.5f) * 80f, 2)
            }
            // Drop guaranteed powerup
            powerUps.add(PowerUp(nextEntityId++, enemy.x, enemy.y, type = PowerUpType.TRIPLE_SHOT))
        } else {
            spawnExplosion(enemy.x, enemy.y, Color(0xFFFF9900), 15)
            // Coin drop
            if (Random.nextFloat() < enemy.coinDropChance) {
                spawnCoin(enemy.x, enemy.y, 1)
            }
            // Rare PowerUp drop
            if (Random.nextFloat() < 0.12f) {
                val types = PowerUpType.entries
                powerUps.add(PowerUp(nextEntityId++, enemy.x, enemy.y, type = types.random()))
            }
        }
    }

    private fun spawnCoin(x: Float, y: Float, value: Int) {
        coins.add(CoinDrop(nextEntityId++, x, y, value = value))
    }

    private fun applyPowerUp(type: PowerUpType) {
        when (type) {
            PowerUpType.TRIPLE_SHOT -> player.tripleShotTicks = 450 // ~7.5 seconds
            PowerUpType.SHIELD -> player.hasShield = true
            PowerUpType.SPEED_BOOST -> player.speedBoostTicks = 350
            PowerUpType.EMP_BOMB -> triggerBomb()
        }
    }

    private fun spawnExplosion(x: Float, y: Float, color: Color, count: Int) {
        for (i in 0 until count) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 5f + 2f
            particles.add(
                Particle(
                    x = x,
                    y = y,
                    vx = cos(angle) * speed,
                    vy = sin(angle) * speed,
                    color = color,
                    life = Random.nextFloat() * 18f + 12f,
                    maxLife = 30f,
                    radius = Random.nextFloat() * 3.5f + 1.5f
                )
            )
        }
    }

    private fun isColliding(
        x1: Float, y1: Float, w1: Float, h1: Float,
        x2: Float, y2: Float, w2: Float, h2: Float
    ): Boolean {
        return (x1 - w1 / 2f < x2 + w2 / 2f &&
                x1 + w1 / 2f > x2 - w2 / 2f &&
                y1 - h1 / 2f < y2 + h2 / 2f &&
                y1 + h1 / 2f > y2 - h2 / 2f)
    }
}
