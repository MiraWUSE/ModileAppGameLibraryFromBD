package com.example.gamelibrary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gamelibrary.data.local.dao.GameDao
import com.example.gamelibrary.data.local.dao.ProfileDao
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.data.local.entity.Profile

@Database(
    entities = [Profile::class, GameEntity::class],
    version = 2,  // 👈 БЫЛО 1, СТАЛО 2 (обязательно!)
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_library_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}