package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CustomBeachSpotEntity
import com.example.data.model.FavoriteSpotEntity
import com.example.data.model.SurfSession

@Database(
    entities = [
        SurfSession::class,
        FavoriteSpotEntity::class,
        CustomBeachSpotEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RisingWavesDatabase : RoomDatabase() {

    abstract fun risingWavesDao(): RisingWavesDao

    companion object {
        @Volatile
        private var INSTANCE: RisingWavesDatabase? = null

        fun getDatabase(context: Context): RisingWavesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RisingWavesDatabase::class.java,
                    "rising_waves_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
