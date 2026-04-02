package com.example.gamelibrary.utils

import com.example.gamelibrary.R

/**
 * Список всех доступных картинок для игр
 * Исправлено: теперь просто List<Int> для картинок
 */
val gameImageResources = listOf(
    R.drawable.ark,
    R.drawable.counterstrike,
    R.drawable.terraria,
    R.drawable.dis,
    R.drawable.dota,
    R.drawable.fortnite,
    R.drawable.pubg,
    R.drawable.hades2,
    R.drawable.liesofp,
    R.drawable.darksouls,
    R.drawable.eldenring,
    R.drawable.littlenightmares2
)

/**
 * Названия картинок по индексам (для отображения)
 */
val gameImageNames = listOf(
    "ARK",
    "CS2",
    "Terraria",
    "Dishonored",
    "Dota 2",
    "Fortnite",
    "PUBG",
    "Hades 2",
    "Lies of P",
    "Dark Souls",
    "Elden Ring",
    "Little Nightmares 2"
)

/**
 * Возвращает случайную картинку
 */
fun getRandomGameImage(): Int {
    return gameImageResources.random()
}

/**
 * Возвращает название картинки по ID
 */
fun getImageNameByResId(resId: Int): String {
    val index = gameImageResources.indexOf(resId)
    return if (index != -1) gameImageNames[index] else "Unknown"
}