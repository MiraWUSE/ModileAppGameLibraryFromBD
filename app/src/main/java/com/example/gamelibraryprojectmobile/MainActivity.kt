package com.example.gamelibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.gamelibrary.data.local.AppDatabase
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.data.local.entity.Profile
import com.example.gamelibrary.data.repository.GameRepository
import com.example.gamelibrary.data.repository.ProfileRepository
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.ViewModelFactory
import com.example.gamelibrary.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val profileRepository by lazy { ProfileRepository(database.profileDao()) }
    private val gameRepository by lazy { GameRepository(database.gameDao()) }
    private val viewModelFactory by lazy { ViewModelFactory(profileRepository, gameRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { initializeDatabase() }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = SteamColors.Background) {
                    com.example.gamelibrary.GameLibraryApp(viewModelFactory = viewModelFactory)
                }
            }
        }
    }

    private suspend fun initializeDatabase() {
        if (profileRepository.allProfiles.first().isEmpty()) {
            profileRepository.insertProfile(Profile(firstName = "Danilka", lastName = "", personalInfo = "Крутышка я • Люблю игры", level = 5, gamesPlayed = 100))
        }

        if (gameRepository.allGames.first().isEmpty()) {
            gameRepository.insertGames(listOf(
                GameEntity(title = "ARK", genre = "Survival", description = "Выживание с динозаврами...", rating = 5.0f, imageResId = R.drawable.ark),
                GameEntity(title = "CS2", genre = "Shooter", description = "Тактический шутер...", rating = 5.0f, imageResId = R.drawable.counterstrike),
                GameEntity(title = "Terraria", genre = "Sandbox", description = "Строительство и приключения...", rating = 4.8f, imageResId = R.drawable.terraria),
                GameEntity(title = "Dishonored", genre = "Action", description = "Стелс-экшен...", rating = 3.9f, imageResId = R.drawable.dis),
                GameEntity(title = "Dota 2", genre = "MOBA", description = "Командная битва...", rating = 5.0f, imageResId = R.drawable.dota),
                GameEntity(title = "Fortnite", genre = "Battle Royale", description = "Королевская битва...", rating = 5.0f, imageResId = R.drawable.fortnite),
                GameEntity(title = "PUBG", genre = "Shooter", description = "Выживание в зоне...", rating = 4.1f, imageResId = R.drawable.pubg),
                GameEntity(title = "Hades 2", genre = "Roguelike", description = "Мифология и подземелья...", rating = 3.5f, imageResId = R.drawable.hades2),
                GameEntity(title = "Lies of P", genre = "Action", description = "Мрачная сказка...", rating = 4.5f, imageResId = R.drawable.liesofp),
                GameEntity(title = "Dark Souls", genre = "RPG", description = "Легендарный хардкор...", rating = 5.0f, imageResId = R.drawable.darksouls),
                GameEntity(title = "Elden Ring", genre = "RPG", description = "Открытый мир...", rating = 5.0f, imageResId = R.drawable.eldenring),
                GameEntity(title = "Little Nightmares 2", genre = "Horror", description = "Мрачный платформер...", rating = 4.3f, imageResId = R.drawable.littlenightmares2)
            ))
        }
    }
}