package com.example.gamelibrary.ui.viewmodel  // 👈 Правильный пакет!

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamelibrary.data.repository.GameRepository
import com.example.gamelibrary.data.repository.ProfileRepository

class ViewModelFactory(
    private val profileRepository: ProfileRepository,
    private val gameRepository: GameRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository) as T
            }
            modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                GameViewModel(gameRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}