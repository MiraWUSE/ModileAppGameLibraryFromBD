package com.example.gamelibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.data.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    val allGames: Flow<List<GameEntity>> = repository.allGames

    fun insertGame(game: GameEntity) {
        viewModelScope.launch { repository.insertGame(game) }
    }

    fun updateGame(game: GameEntity) {
        viewModelScope.launch { repository.updateGame(game) }
    }

    fun deleteGame(game: GameEntity) {
        viewModelScope.launch { repository.deleteGame(game) }
    }

    fun getGameById(gameId: Int): Flow<GameEntity?> {
        return repository.getGameById(gameId)
    }

    fun searchGames(query: String): Flow<List<GameEntity>> {
        return repository.searchGames(query)
    }
}