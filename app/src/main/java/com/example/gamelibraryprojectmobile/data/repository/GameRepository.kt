package com.example.gamelibrary.data.repository

import com.example.gamelibrary.data.local.dao.GameDao
import com.example.gamelibrary.data.local.entity.GameEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<GameEntity>> = gameDao.getAllGames()

    suspend fun insertGame(game: GameEntity) {
        gameDao.insertGame(game)
    }

    // 👇 ДОБАВЬ ЭТОТ МЕТОД:
    suspend fun insertGames(games: List<GameEntity>) {  // ← Для вставки списка!
        gameDao.insertGames(games)
    }

    suspend fun updateGame(game: GameEntity) {
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: GameEntity) {
        gameDao.deleteGame(game)
    }

    fun getGameById(gameId: Int): Flow<GameEntity?> {
        return gameDao.getGameById(gameId)
    }

    fun searchGames(query: String): Flow<List<GameEntity>> {
        return gameDao.searchGames(query)
    }

    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }


}