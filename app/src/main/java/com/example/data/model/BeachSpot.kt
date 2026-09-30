package com.example.data.model

enum class TideStatus(val label: String, val arrow: String) {
    RISING("Rising Tide", "▲"),
    FALLING("Falling Tide", "▼"),
    HIGH_SLACK("High Slack", "■"),
    LOW_SLACK("Low Slack", "■")
}

data class BeachSpot(
    val id: String,
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val baseTideHeight: Double, // in meters
    val tideRange: Double, // tidal oscillation amplitude in meters
    val baseWaveHeight: Double, // wave height in meters
    val swellPeriod: Int, // seconds
    val swellDirection: String,
    val windKnots: Int,
    val windDirection: String,
    val waterTempC: Int,
    val airTempC: Int,
    val difficulty: String,
    val bestTide: String,
    val description: String,
    val isCustom: Boolean = false
) {
    // Calculates the dynamic water height at a given fraction of the 12.42-hour semi-diurnal tidal cycle
    fun calculateTideAtHour(hourOfDay: Float): Double {
        // Semi-diurnal M2 tide cycle is ~12.42 hours (approx 2 high and 2 low tides per day)
        val cyclePhase = ((hourOfDay % 12.42f) / 12.42f) * 2 * Math.PI
        val tideVariation = (tideRange / 2.0) * Math.sin(cyclePhase)
        return (baseTideHeight + tideVariation).coerceAtLeast(0.1)
    }

    // Determine if tide is rising or falling at a given hour
    fun getTideStatusAtHour(hourOfDay: Float): TideStatus {
        val current = calculateTideAtHour(hourOfDay)
        val next = calculateTideAtHour(hourOfDay + 0.1f)
        val diff = next - current
        return when {
            diff > 0.015 -> TideStatus.RISING
            diff < -0.015 -> TideStatus.FALLING
            current > baseTideHeight -> TideStatus.HIGH_SLACK
            else -> TideStatus.LOW_SLACK
        }
    }

    fun getSurfRating(): Int {
        // Rating out of 5 based on swell period, wind, and wave height
        var score = 3
        if (swellPeriod >= 13) score++
        if (windKnots <= 12) score++
        if (windKnots > 24) score--
        if (baseWaveHeight < 0.6) score--
        return score.coerceIn(1, 5)
    }
}
