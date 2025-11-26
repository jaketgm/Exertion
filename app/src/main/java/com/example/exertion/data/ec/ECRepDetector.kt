package com.example.exertion.data.ec

class ECRepDetector {
    private var lastSampleTime: Long? = null
    private var lastRom: Float? = null

    private var currentPhase: ECPhase = ECPhase.IDLE
    private var phaseStartTime: Long? = null

    private var eccStartTime: Long? = null
    private var conStartTime: Long? = null

    private var lastBottomTime: Long? = null
    private var lastTopTime: Long? = null

    private var repCounter = 0

    private val lowThresh = 0.25f // ROM bottom
    private val highThresh = 0.75f // ROM top
    private val velThresh = 0.15f // "significant movement"

    fun onSample(sample: ECFrameMetrics): ECRepData? {
        val t = sample.timestampMs
        val rom = sample.romFraction.coerceIn(0f, 1f)
        val vel = sample.velocity

        val lastRomVal = lastRom
        lastRom = rom

        if (lastSampleTime == null) {
            lastSampleTime = t
            return null
        }

        val newPhase = when {
            vel > velThresh  -> ECPhase.CONCENTRIC
            vel < -velThresh -> ECPhase.ECCENTRIC
            else             -> ECPhase.IDLE
        }

        if (newPhase != currentPhase) {
            currentPhase = newPhase
            phaseStartTime = t
        }

        // detect the bottom if we cross from above highThresh to below lowThresh
        if (lastRomVal != null && lastRomVal > highThresh && rom < lowThresh) {
            lastBottomTime = t
            eccStartTime = phaseStartTime ?: (t - 200)
        }

        // vice versa
        if (lastRomVal != null && lastRomVal < lowThresh && rom > highThresh) {
            lastTopTime = t
            conStartTime = phaseStartTime ?: (t - 200)
        }

        // full rep => both a bottom & top in proper order
        val bottom = lastBottomTime
        val top = lastTopTime
        val eccStart = eccStartTime
        val conStart = conStartTime

        if (bottom != null && top != null && eccStart != null && conStart != null && bottom < top) {
            repCounter += 1

            val eccMs = (bottom - eccStart).coerceAtLeast(0)
            val conMs = (top - conStart).coerceAtLeast(0)
            val tutMs = (top - eccStart).coerceAtLeast(0)

            val avgVel = if (tutMs > 0) 1.0 * (highThresh - lowThresh) / (tutMs / 1000.0) else 0.0
            val romDeg = (highThresh - lowThresh) * 80.0

            lastBottomTime = null
            lastTopTime = null
            eccStartTime = null
            conStartTime = null

            return ECRepData(
                repIndex = repCounter,
                eccentricMs = eccMs.toDouble(),
                concentricMs = conMs.toDouble(),
                tutMs = tutMs.toDouble(),
                velocity = avgVel,
                romDeg = romDeg
            )
        }

        lastSampleTime = t
        return null
    }
}
