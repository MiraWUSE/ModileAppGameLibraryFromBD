package com.example.gamelibrary.data

import com.example.gamelibrary.R
import com.example.gamelibrary.model.Game

object MockData {
    val games = listOf(
        Game(1, "ARK", "Survival", 5.0f, "Выживание с динозаврами.", R.drawable.ark),
        Game(2, "CS2", "Shooter", 5.0f, "Тактический командный шутер.", R.drawable.counterstrike),
        Game(3, "Terraria", "Sandbox", 4.8f, "Строительство и приключения.", R.drawable.terraria),
        Game(4, "Dishonored", "Action", 3.9f, "Стелс-экшен в мрачном городе.", R.drawable.dis),
        Game(5, "Dota 2", "MOBA", 5.0f, "Командная битва на арене.", R.drawable.dota),
        Game(6, "Fortnite", "Battle Royale", 5.0f, "Королевская битва на острове.", R.drawable.fortnite),
        Game(7, "PUBG", "Shooter", 4.1f, "Выживание в зоне отчуждения.", R.drawable.pubg),
        Game(8, "Hades 2", "Roguelike", 3.5f, "Мифология и подземелья.", R.drawable.hades2),
        Game(9, "Lies of P", "Action", 4.5f, "Мрачная сказка о Пиноккио в стиле soulslike.", R.drawable.liesofp),
        Game(10, "Dark Souls", "RPG", 5.0f, "Легендарный хардкорный экшен.", R.drawable.darksouls),
        Game(11, "Elden Ring", "RPG", 5.0f, "Открытый мир от создателей Dark Souls.", R.drawable.eldenring),
        Game(12, "Little Nightmares 2", "Horror", 4.3f, "Мрачный платформер с головоломками.", R.drawable.littlenightmares2))
}