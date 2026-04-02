package com.example.gamelibrary.data.local.entity  // 👈 Правильный пакет!

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val personalInfo: String,
    val avatarUrl: String = "",
    val level: Int = 1,
    val gamesPlayed: Int = 0
)