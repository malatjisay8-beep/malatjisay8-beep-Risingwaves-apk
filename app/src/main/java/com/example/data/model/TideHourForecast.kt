package com.example.data.model

data class TideHourForecast(
    val hour: Int, // 0..23
    val timeLabel: String, // e.g. "06:00"
    val tideHeightMeters: Double,
    val isRising: Boolean,
    val waveHeightMeters: Double,
    val windKnots: Int,
    val surfRating: Int // 1..5
)

data class TideExtremum(
    val type: String, // "High Tide" or "Low Tide"
    val timeLabel: String,
    val heightMeters: Double,
    val minutesFromNow: Int
)
