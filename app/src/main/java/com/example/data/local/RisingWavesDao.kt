package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomBeachSpotEntity
import com.example.data.model.FavoriteSpotEntity
import com.example.data.model.SurfSession
import kotlinx.coroutines.flow.Flow

@Dao
interface RisingWavesDao {

    // Surf Sessions
    @Query("SELECT * FROM surf_sessions ORDER BY dateTimestamp DESC")
    fun getAllSurfSessions(): Flow<List<SurfSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurfSession(session: SurfSession): Long

    @Update
    suspend fun updateSurfSession(session: SurfSession)

    @Delete
    suspend fun deleteSurfSession(session: SurfSession)

    @Query("DELETE FROM surf_sessions WHERE id = :sessionId")
    suspend fun deleteSurfSessionById(sessionId: Long)

    // Favorites
    @Query("SELECT * FROM favorite_spots")
    fun getAllFavoriteSpots(): Flow<List<FavoriteSpotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteSpot(favorite: FavoriteSpotEntity)

    @Query("DELETE FROM favorite_spots WHERE spotId = :spotId")
    suspend fun removeFavoriteSpot(spotId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_spots WHERE spotId = :spotId)")
    suspend fun isFavoriteSpot(spotId: String): Boolean

    // Custom Spots
    @Query("SELECT * FROM custom_beach_spots ORDER BY name ASC")
    fun getAllCustomSpots(): Flow<List<CustomBeachSpotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomSpot(spot: CustomBeachSpotEntity)

    @Query("DELETE FROM custom_beach_spots WHERE id = :id")
    suspend fun deleteCustomSpotById(id: String)
}
