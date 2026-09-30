package com.example.data.repository

import android.content.Context
import com.example.data.local.RisingWavesDao
import com.example.data.model.BeachSpot
import com.example.data.model.CustomBeachSpotEntity
import com.example.data.model.FavoriteSpotEntity
import com.example.data.model.SurfSession
import com.example.data.model.TideExtremum
import com.example.data.model.TideHourForecast
import com.example.data.model.TideStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar

class WavesRepository(
    private val dao: RisingWavesDao,
    private val context: Context
) {
    // Predefined iconic marine spots
    private val defaultSpots = listOf(
        BeachSpot(
            id = "spot_jbay",
            name = "Jeffreys Bay (Superbank)",
            region = "Eastern Cape",
            country = "South Africa",
            latitude = -34.0506,
            longitude = 24.9189,
            baseTideHeight = 1.4,
            tideRange = 1.6,
            baseWaveHeight = 2.2,
            swellPeriod = 15,
            swellDirection = "SSW",
            windKnots = 11,
            windDirection = "Offshore SW",
            waterTempC = 19,
            airTempC = 23,
            difficulty = "Advanced",
            bestTide = "Mid to High (Rising)",
            description = "World famous high-performance right-hand point break. Pristine cylindrical walls on incoming rising tide."
        ),
        BeachSpot(
            id = "spot_muizenberg",
            name = "Muizenberg Beach",
            region = "Cape Town",
            country = "South Africa",
            latitude = -34.1081,
            longitude = 18.4719,
            baseTideHeight = 1.3,
            tideRange = 1.5,
            baseWaveHeight = 1.2,
            swellPeriod = 12,
            swellDirection = "SE",
            windKnots = 9,
            windDirection = "Offshore NW",
            waterTempC = 17,
            airTempC = 21,
            difficulty = "All Levels",
            bestTide = "All Tides (Best on Rising)",
            description = "Gentle rolling beach break with historic colorful beach huts. Renowned surf mecca in False Bay."
        ),
        BeachSpot(
            id = "spot_durban",
            name = "Durban North Beach",
            region = "KwaZulu-Natal",
            country = "South Africa",
            latitude = -29.8519,
            longitude = 31.0373,
            baseTideHeight = 1.5,
            tideRange = 1.8,
            baseWaveHeight = 1.8,
            swellPeriod = 13,
            swellDirection = "ESE",
            windKnots = 12,
            windDirection = "Offshore W",
            waterTempC = 24,
            airTempC = 26,
            difficulty = "Intermediate",
            bestTide = "Mid Tide (Rising)",
            description = "Warm Indian Ocean rollers and hollow barreling piers. Excellent morning offshore winds."
        ),
        BeachSpot(
            id = "spot_pipeline",
            name = "Banzai Pipeline",
            region = "Oahu, Hawaii",
            country = "USA",
            latitude = 21.6649,
            longitude = -158.0538,
            baseTideHeight = 0.8,
            tideRange = 0.9,
            baseWaveHeight = 3.1,
            swellPeriod = 16,
            swellDirection = "WNW",
            windKnots = 8,
            windDirection = "Trade ENE",
            waterTempC = 25,
            airTempC = 27,
            difficulty = "Pro Only",
            bestTide = "Mid Tide",
            description = "The crown jewel of heavy reef barrels. Majestic hollow waves breaking over shallow coral reef."
        ),
        BeachSpot(
            id = "spot_mavericks",
            name = "Mavericks",
            region = "Half Moon Bay, CA",
            country = "USA",
            latitude = 37.4952,
            longitude = -122.5020,
            baseTideHeight = 1.6,
            tideRange = 2.1,
            baseWaveHeight = 4.2,
            swellPeriod = 17,
            swellDirection = "WNW",
            windKnots = 14,
            windDirection = "Light Offshore E",
            waterTempC = 13,
            airTempC = 16,
            difficulty = "Pro Only",
            bestTide = "Low to Mid (Rising)",
            description = "Colossal cold-water titan wave off Pillar Point reef. Tremendous hydraulic power and deep swells."
        ),
        BeachSpot(
            id = "spot_bondi",
            name = "Bondi Beach",
            region = "Sydney, NSW",
            country = "Australia",
            latitude = -33.8915,
            longitude = 151.2767,
            baseTideHeight = 1.2,
            tideRange = 1.4,
            baseWaveHeight = 1.4,
            swellPeriod = 11,
            swellDirection = "SSE",
            windKnots = 10,
            windDirection = "Offshore W",
            waterTempC = 21,
            airTempC = 24,
            difficulty = "All Levels",
            bestTide = "Mid Tide",
            description = "Vibrant iconic crescent bay. Dynamic sandbars create playful peaks from South Bondi to the skate park."
        ),
        BeachSpot(
            id = "spot_supertubos",
            name = "Supertubos",
            region = "Peniche",
            country = "Portugal",
            latitude = 39.3456,
            longitude = -9.3628,
            baseTideHeight = 1.8,
            tideRange = 2.4,
            baseWaveHeight = 2.0,
            swellPeriod = 14,
            swellDirection = "W",
            windKnots = 10,
            windDirection = "Offshore E",
            waterTempC = 16,
            airTempC = 20,
            difficulty = "Advanced",
            bestTide = "Mid to High (Incoming)",
            description = "European barrel capital. Fast throwing sandbar tubes that break right along the coast."
        )
    )

    // Flow of all spots combined with favorites and custom user spots
    val allSpotsFlow: Flow<List<BeachSpot>> = combine(
        dao.getAllFavoriteSpots(),
        dao.getAllCustomSpots()
    ) { favorites, customSpots ->
        val customConverted = customSpots.map {
            BeachSpot(
                id = it.id,
                name = it.name,
                region = it.region,
                country = it.country,
                latitude = it.latitude,
                longitude = it.longitude,
                baseTideHeight = 1.2,
                tideRange = it.tideRange,
                baseWaveHeight = it.baseWaveHeight,
                swellPeriod = 12,
                swellDirection = "SW",
                windKnots = 10,
                windDirection = "Offshore",
                waterTempC = 20,
                airTempC = 22,
                difficulty = "Custom",
                bestTide = it.bestTide,
                description = "Custom registered coastal spot coordinates (${it.latitude}, ${it.longitude})",
                isCustom = true
            )
        }
        defaultSpots + customConverted
    }

    val favoritesFlow: Flow<List<FavoriteSpotEntity>> = dao.getAllFavoriteSpots()

    suspend fun toggleFavorite(spotId: String) {
        if (dao.isFavoriteSpot(spotId)) {
            dao.removeFavoriteSpot(spotId)
        } else {
            dao.addFavoriteSpot(FavoriteSpotEntity(spotId))
        }
    }

    suspend fun addCustomSpot(spot: CustomBeachSpotEntity) {
        dao.insertCustomSpot(spot)
    }

    suspend fun deleteCustomSpot(id: String) {
        dao.deleteCustomSpotById(id)
    }

    // Surf Sessions
    val allSessionsFlow: Flow<List<SurfSession>> = dao.getAllSurfSessions()

    suspend fun logSession(session: SurfSession): Long {
        return dao.insertSurfSession(session)
    }

    suspend fun deleteSession(id: Long) {
        dao.deleteSurfSessionById(id)
    }

    // Generates 24-hour tidal and swell forecast for a spot
    fun get24HourForecast(spot: BeachSpot): List<TideHourForecast> {
        val list = mutableListOf<TideHourForecast>()
        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)

        for (i in 0 until 24) {
            val hour = (currentHour + i) % 24
            val hourFloat = hour.toFloat()
            val tide = spot.calculateTideAtHour(hourFloat)
            val status = spot.getTideStatusAtHour(hourFloat)
            val waveHeight = spot.baseWaveHeight + (0.3 * Math.sin(i * 0.4))
            val wind = (spot.windKnots + (i % 5) - 2).coerceAtLeast(4)

            list.add(
                TideHourForecast(
                    hour = hour,
                    timeLabel = String.format("%02d:00", hour),
                    tideHeightMeters = tide,
                    isRising = (status == TideStatus.RISING),
                    waveHeightMeters = Math.max(0.5, waveHeight),
                    windKnots = wind,
                    surfRating = spot.getSurfRating()
                )
            )
        }
        return list
    }

    // Calculates next High Tide and Low Tide events
    fun getNextTideExtrema(spot: BeachSpot): Pair<TideExtremum, TideExtremum> {
        val cal = Calendar.getInstance()
        val currentHourFloat = cal.get(Calendar.HOUR_OF_DAY) + (cal.get(Calendar.MINUTE) / 60f)

        // Find next crest (high tide) and trough (low tide)
        // High tide occurs when sin(2*pi*h / 12.42) = 1 => 2*pi*h/12.42 = pi/2 + 2k*pi => h = 12.42*(1/4 + k)
        val cycle = 12.42f
        val quarter = cycle / 4f
        val threeQuarter = (3f * cycle) / 4f

        var nextHighHour = quarter
        while (nextHighHour <= currentHourFloat) {
            nextHighHour += cycle
        }
        val highMinutesAway = ((nextHighHour - currentHourFloat) * 60).toInt().coerceAtLeast(5)

        var nextLowHour = threeQuarter
        while (nextLowHour <= currentHourFloat) {
            nextLowHour += cycle
        }
        val lowMinutesAway = ((nextLowHour - currentHourFloat) * 60).toInt().coerceAtLeast(5)

        val highTimeCal = Calendar.getInstance().apply { add(Calendar.MINUTE, highMinutesAway) }
        val lowTimeCal = Calendar.getInstance().apply { add(Calendar.MINUTE, lowMinutesAway) }

        val highExtremum = TideExtremum(
            type = "High Tide",
            timeLabel = String.format("%02d:%02d", highTimeCal.get(Calendar.HOUR_OF_DAY), highTimeCal.get(Calendar.MINUTE)),
            heightMeters = spot.baseTideHeight + (spot.tideRange / 2.0),
            minutesFromNow = highMinutesAway
        )

        val lowExtremum = TideExtremum(
            type = "Low Tide",
            timeLabel = String.format("%02d:%02d", lowTimeCal.get(Calendar.HOUR_OF_DAY), lowTimeCal.get(Calendar.MINUTE)),
            heightMeters = (spot.baseTideHeight - (spot.tideRange / 2.0)).coerceAtLeast(0.2),
            minutesFromNow = lowMinutesAway
        )

        return Pair(highExtremum, lowExtremum)
    }

    fun getDefaultSpots(): List<BeachSpot> = defaultSpots
}
