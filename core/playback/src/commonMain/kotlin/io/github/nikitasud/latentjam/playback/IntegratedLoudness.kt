/* Copyright (c) 2026 LatentJam Project. SPDX-License-Identifier: Apache-2.0 */
package io.github.nikitasud.latentjam.playback

import kotlin.math.pow

/** Which direction loudness normalization may adjust a track. */
public enum class NormalizationMode { ONLY_DECREASE, DECREASE_AND_INCREASE, ONLY_INCREASE }

/**
 * Full-track EBU R128 measurement. Never construct this from the legacy sampled RMS cache. [sound]
 * is where the track's sound begins and ends, found in the same pass; null when it has none or was
 * measured before sound edges were. [fade] is how the track ends, for a fade entry ([FadeEnd]),
 * from the same pass.
 */
public data class IntegratedLoudness(
    public val lufs: Float,
    public val truePeakDbtp: Float,
    public val sound: SoundSpan? = null,
    public val fade: FadeEnd? = null,
) {
    public fun isValid(): Boolean = lufs.isFinite() && lufs in -100f..10f &&
        truePeakDbtp.isFinite() && truePeakDbtp in -160f..40f

    /** Limit adjustments to the selected direction and avoid boosting above -1 dBTP.
     * Subsequent EQ has its own sample-peak limiter. */
    public fun normalizationVolume(mode: NormalizationMode = NormalizationMode.ONLY_DECREASE): Float {
        if (!isValid()) return 1f
        val targetGain = -14f - lufs
        // The iOS per-deck gain unit supports up to +24 dB; use the same safe limit on both platforms.
        val peakLimitedGain = minOf(targetGain, -1f - truePeakDbtp, 24f)
        val gainDb = when (mode) {
            NormalizationMode.ONLY_DECREASE -> minOf(0f, peakLimitedGain)
            NormalizationMode.DECREASE_AND_INCREASE -> peakLimitedGain
            NormalizationMode.ONLY_INCREASE -> maxOf(0f, peakLimitedGain)
        }
        return 10.0.pow(gainDb / 20.0).toFloat()
    }
}

/** Background-only native meter. Each owner must close it, including cancellation/failure paths. */
internal expect class NativeLoudnessMeter(sampleRate: Int, channels: Int) {
    fun add(samples: FloatArray, frames: Int): Boolean
    fun finish(): IntegratedLoudness?
    fun close()
}

/** Resource caps are refusals, never a license to label a partial scan as integrated LUFS. */
internal object LoudnessScanLimits {
    const val CHUNK_FRAMES = 4096
    const val MAX_DURATION_MS = 6L * 60 * 60 * 1000
    const val WALL_BUDGET_MS = 120_000L
    const val IDLE_BUDGET_MS = 5_000L

    /**
     * [sourceEnded]: the file itself ran out — the extractor's end of stream, a read that found no
     * more audio — rather than the scan stopping short. A container duration is then only an
     * estimate (a CBR MP3 without a seek table guesses from the file size, junk tail included), and
     * what decoded is what the player plays: a shorter result stands, down to [MIN_ENDED_SHARE] of
     * the estimate. The file's size is part of its audio identity, so one still being written is
     * measured again once it grows.
     */
    fun complete(frames: Long, rate: Int, expectedDurationMs: Long?, sourceEnded: Boolean = false): Boolean {
        if (rate !in 8000..192000 || frames < (rate + 5L) / 10 * 4) return false
        if (frames > MAX_DURATION_MS * rate / 1000) return false
        val duration = frames * 1000 / rate
        // Container durations can include encoder delay/padding and rounding. Gross truncation,
        // including an early apparent EOS, must not be cached as a complete measurement.
        val expected = expectedDurationMs?.takeIf { it > 0 } ?: return true
        if (kotlin.math.abs(duration - expected) <= maxOf(2_000L, expected / 50)) return true
        return sourceEnded && duration < expected && duration >= expected * MIN_ENDED_SHARE / 100
    }

    /** The least of a container's estimated duration a file that ran out may hold, in percent. */
    const val MIN_ENDED_SHARE = 50L
}
