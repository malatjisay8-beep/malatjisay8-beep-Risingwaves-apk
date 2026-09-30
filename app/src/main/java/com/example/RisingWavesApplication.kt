package com.example

import android.app.Application
import com.example.data.local.RisingWavesDatabase
import com.example.data.repository.WavesRepository

class RisingWavesApplication : Application() {
    val database: RisingWavesDatabase by lazy {
        RisingWavesDatabase.getDatabase(this)
    }

    val repository: WavesRepository by lazy {
        WavesRepository(database.risingWavesDao(), this)
    }
}
