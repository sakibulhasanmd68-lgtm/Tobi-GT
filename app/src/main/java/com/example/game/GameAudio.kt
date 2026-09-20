package com.example.game

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class GameAudio {
    var isSoundEnabled: Boolean = true
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playLaserSound() {
        if (!isSoundEnabled) return
        scope.launch {
            generateTone(startFreq = 880f, endFreq = 220f, durationMs = 70, volume = 0.25f)
        }
    }

    fun playExplosionSound() {
        if (!isSoundEnabled) return
        scope.launch {
            generateNoise(durationMs = 150, volume = 0.35f)
        }
    }

    fun playCoinSound() {
        if (!isSoundEnabled) return
        scope.launch {
            generateTone(startFreq = 987f, endFreq = 1318f, durationMs = 80, volume = 0.3f)
        }
    }

    fun playPowerUpSound() {
        if (!isSoundEnabled) return
        scope.launch {
            generateArpeggio(listOf(523f, 659f, 783f, 1046f), noteMs = 40, volume = 0.35f)
        }
    }

    fun playBossAlarmSound() {
        if (!isSoundEnabled) return
        scope.launch {
            generateTone(startFreq = 300f, endFreq = 600f, durationMs = 250, volume = 0.4f)
        }
    }

    private fun generateTone(startFreq: Float, endFreq: Float, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
            val buffer = ShortArray(numSamples)
            var currentFreq = startFreq
            val freqStep = (endFreq - startFreq) / numSamples
            var phase = 0.0

            for (i in 0 until numSamples) {
                currentFreq += freqStep
                phase += 2.0 * Math.PI * currentFreq / sampleRate
                val sample = (sin(phase) * 32767 * volume).toInt().toShort()
                buffer[i] = sample
            }

            writeAndPlay(buffer, sampleRate)
        } catch (_: Exception) {}
    }

    private fun generateNoise(durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt().coerceAtLeast(1)
            val buffer = ShortArray(numSamples)
            var lastSample = 0.0

            for (i in 0 until numSamples) {
                val decay = 1.0 - (i.toDouble() / numSamples)
                val white = (Math.random() * 2.0 - 1.0)
                // Low-pass filtered noise for explosion rumble
                lastSample = (lastSample + (0.1 * white)) / 1.1
                buffer[i] = (lastSample * 32767 * volume * decay).toInt().toShort()
            }

            writeAndPlay(buffer, sampleRate)
        } catch (_: Exception) {}
    }

    private fun generateArpeggio(freqs: List<Float>, noteMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val samplesPerNote = (sampleRate * (noteMs / 1000f)).toInt().coerceAtLeast(1)
            val totalSamples = samplesPerNote * freqs.size
            val buffer = ShortArray(totalSamples)

            freqs.forEachIndexed { noteIndex, freq ->
                var phase = 0.0
                for (i in 0 until samplesPerNote) {
                    phase += 2.0 * Math.PI * freq / sampleRate
                    val sample = (sin(phase) * 32767 * volume).toInt().toShort()
                    buffer[noteIndex * samplesPerNote + i] = sample
                }
            }

            writeAndPlay(buffer, sampleRate)
        } catch (_: Exception) {}
    }

    private fun writeAndPlay(buffer: ShortArray, sampleRate: Int) {
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
                        .setSampleRate(sampleRate)
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
                    t?.release()
                }
                override fun onPeriodicNotification(t: AudioTrack?) {}
            })
        } catch (_: Exception) {}
    }
}
