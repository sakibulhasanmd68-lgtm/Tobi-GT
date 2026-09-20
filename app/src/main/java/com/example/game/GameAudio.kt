package com.example.game

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.sin

/**
 * Ultra-low latency, zero-lag procedural sound effect engine for Tobi GT.
 * Precomputes PCM sample arrays once on initialization, rate-limits playback,
 * and caps concurrent AudioTrack instances to eliminate audio thread jank.
 */
class GameAudio {
    var isSoundEnabled: Boolean = true
    private val scope = CoroutineScope(Dispatchers.Default)

    private val activeTracks = AtomicInteger(0)
    private val maxConcurrentTracks = 3

    private var lastLaserTime = 0L
    private var lastExplosionTime = 0L
    private var lastCoinTime = 0L

    companion object {
        private const val SAMPLE_RATE = 22050

        // Pre-computed PCM sample buffers (generated once, zero CPU overhead during gameplay)
        private val laserBuffer: ShortArray by lazy {
            createToneBuffer(startFreq = 880f, endFreq = 220f, durationMs = 60, volume = 0.22f)
        }
        private val explosionBuffer: ShortArray by lazy {
            createNoiseBuffer(durationMs = 120, volume = 0.28f)
        }
        private val coinBuffer: ShortArray by lazy {
            createToneBuffer(startFreq = 987f, endFreq = 1318f, durationMs = 70, volume = 0.25f)
        }
        private val powerUpBuffer: ShortArray by lazy {
            createArpeggioBuffer(listOf(523f, 659f, 783f, 1046f), noteMs = 35, volume = 0.3f)
        }
        private val bossAlarmBuffer: ShortArray by lazy {
            createToneBuffer(startFreq = 300f, endFreq = 600f, durationMs = 200, volume = 0.35f)
        }

        private fun createToneBuffer(startFreq: Float, endFreq: Float, durationMs: Int, volume: Float): ShortArray {
            val numSamples = (SAMPLE_RATE * (durationMs / 1000f)).toInt().coerceAtLeast(1)
            val buffer = ShortArray(numSamples)
            var currentFreq = startFreq
            val freqStep = (endFreq - startFreq) / numSamples
            var phase = 0.0

            for (i in 0 until numSamples) {
                currentFreq += freqStep
                phase += 2.0 * Math.PI * currentFreq / SAMPLE_RATE
                buffer[i] = (sin(phase) * 32767 * volume).toInt().toShort()
            }
            return buffer
        }

        private fun createNoiseBuffer(durationMs: Int, volume: Float): ShortArray {
            val numSamples = (SAMPLE_RATE * (durationMs / 1000f)).toInt().coerceAtLeast(1)
            val buffer = ShortArray(numSamples)
            var lastSample = 0.0

            for (i in 0 until numSamples) {
                val decay = 1.0 - (i.toDouble() / numSamples)
                val white = (Math.random() * 2.0 - 1.0)
                lastSample = (lastSample + (0.1 * white)) / 1.1
                buffer[i] = (lastSample * 32767 * volume * decay).toInt().toShort()
            }
            return buffer
        }

        private fun createArpeggioBuffer(freqs: List<Float>, noteMs: Int, volume: Float): ShortArray {
            val samplesPerNote = (SAMPLE_RATE * (noteMs / 1000f)).toInt().coerceAtLeast(1)
            val totalSamples = samplesPerNote * freqs.size
            val buffer = ShortArray(totalSamples)

            freqs.forEachIndexed { noteIndex, freq ->
                var phase = 0.0
                for (i in 0 until samplesPerNote) {
                    phase += 2.0 * Math.PI * freq / SAMPLE_RATE
                    buffer[noteIndex * samplesPerNote + i] = (sin(phase) * 32767 * volume).toInt().toShort()
                }
            }
            return buffer
        }
    }

    fun playLaserSound() {
        if (!isSoundEnabled) return
        val now = System.currentTimeMillis()
        if (now - lastLaserTime < 110) return // Throttled to prevent audio flinger starvation
        lastLaserTime = now
        playBufferAsync(laserBuffer)
    }

    fun playExplosionSound() {
        if (!isSoundEnabled) return
        val now = System.currentTimeMillis()
        if (now - lastExplosionTime < 100) return
        lastExplosionTime = now
        playBufferAsync(explosionBuffer)
    }

    fun playCoinSound() {
        if (!isSoundEnabled) return
        val now = System.currentTimeMillis()
        if (now - lastCoinTime < 80) return
        lastCoinTime = now
        playBufferAsync(coinBuffer)
    }

    fun playPowerUpSound() {
        if (!isSoundEnabled) return
        playBufferAsync(powerUpBuffer)
    }

    fun playBossAlarmSound() {
        if (!isSoundEnabled) return
        playBufferAsync(bossAlarmBuffer)
    }

    private fun playBufferAsync(buffer: ShortArray) {
        if (activeTracks.get() >= maxConcurrentTracks) return

        activeTracks.incrementAndGet()
        scope.launch {
            try {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                track.setNotificationMarkerPosition(buffer.size)
                track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                    override fun onMarkerReached(t: AudioTrack?) {
                        try {
                            t?.stop()
                            t?.release()
                        } catch (_: Exception) {}
                        activeTracks.decrementAndGet()
                    }
                    override fun onPeriodicNotification(t: AudioTrack?) {}
                })
            } catch (_: Exception) {
                activeTracks.decrementAndGet()
            }
        }
    }
}
