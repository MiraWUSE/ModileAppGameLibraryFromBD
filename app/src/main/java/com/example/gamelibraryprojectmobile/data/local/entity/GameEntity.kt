package com.example.gamelibrary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val genre: String,
    val description: String,
    val rating: Float,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val hoursPlayed: Float = 0f,
    val imageResId: Int = 0
)