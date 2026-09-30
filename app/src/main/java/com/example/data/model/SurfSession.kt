package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surf_sessions")
data class SurfSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val spotName: String,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int = 60,
    val waveCount: Int = 0,
    val waveHeightMeters: Double = 1.5,
    val boardType: String = "Shortboard", // Shortboard, Longboard, Fish, Funboard, SUP, Bodyboard
    val ratingStars: Int = 4, // 1 to 5
    val tideCondition: String = "Rising", // Rising, Falling, High, Low
    val notes: String = ""
)
