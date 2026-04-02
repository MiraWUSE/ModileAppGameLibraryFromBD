package com.example.gamelibrary.model

data class Game(
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Float,
    val description: String,
    val imageResId: Int
)