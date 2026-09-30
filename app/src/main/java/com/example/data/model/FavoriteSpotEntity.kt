package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_spots")
data class FavoriteSpotEntity(
    @PrimaryKey
    val spotId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_beach_spots")
data class CustomBeachSpotEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val baseWaveHeight: Double,
    val tideRange: Double,
    val bestTide: String
)
