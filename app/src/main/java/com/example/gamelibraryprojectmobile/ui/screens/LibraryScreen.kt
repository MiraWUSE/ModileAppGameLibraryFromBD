package com.example.gamelibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.utils.gameImageResources

@Composable
fun LibraryScreen(
    onGameClick: (Int) -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    val games by gameViewModel.allGames.collectAsState(initial = emptyList())
    var editingGame by remember { mutableStateOf<GameEntity?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().background(SteamColors.Background).padding(16.dp)
    ) {
        Text("Моя библиотека", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${games.size} игр в библиотеке", fontSize = 14.sp, color = SteamColors.TextSecondary)
        Spacer(modifier = Modifier.height(20.dp))

        if (games.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("🎮 Библиотека пуста...\nДобавь игры на главной!", fontSize = 16.sp, color = SteamColors.TextSecondary, textAlign = TextAlign.Center)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(games) { game ->
                    LibraryGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        onEdit = { editingGame = game },
                        onDelete = { gameViewModel.deleteGame(game) }
                    )
                }
            }
        }
    }

    if (editingGame != null) {
        EditGameDialog(
            game = editingGame!!,
            onConfirm = { updatedGame ->
                gameViewModel.updateGame(updatedGame)
                editingGame = null
            },
            onDismiss = { editingGame = null }
        )
    }
}

@Composable
fun LibraryGameCard(game: GameEntity, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.height(200.dp).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = SteamColors.Primary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color(0xFFEF5350), modifier = Modifier.size(18.dp))
                }
            }
            Image(
                painter = painterResource(id = game.imageResId.takeIf { it != 0 } ?: com.example.gamelibrary.utils.getRandomGameImage()),
                contentDescription = game.title,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(game.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary, textAlign = TextAlign.Center, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(game.genre, fontSize = 12.sp, color = SteamColors.TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text("★ ${game.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SteamColors.Rating)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameDialog(game: GameEntity, onConfirm: (GameEntity) -> Unit, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf(game.title) }
    var genre by remember { mutableStateOf(game.genre) }
    var description by remember { mutableStateOf(game.description) }
    var rating by remember { mutableStateOf(game.rating.toString()) }
    var selectedImageResId by remember { mutableStateOf(game.imageResId.takeIf { it != 0 } ?: com.example.gamelibrary.utils.getRandomGameImage()) }
    var showImageSelector by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Редактировать игру", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Обложка:", fontSize = 14.sp, color = SteamColors.TextSecondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = selectedImageResId), contentDescription = "Обложка", modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { showImageSelector = true }) { Text("Выбрать", color = SteamColors.Primary) }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Название", color = SteamColors.TextSecondary) }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Жанр", color = SteamColors.TextSecondary) }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Описание", color = SteamColors.TextSecondary) }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth(), maxLines = 3)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Рейтинг (0-5)", color = SteamColors.TextSecondary) }, colors = textFieldColors(), modifier = Modifier.fillMaxWidth(), keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal))
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Отмена", color = SteamColors.TextSecondary) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val parsedRating = rating.toFloatOrNull()?.coerceIn(0f, 5f) ?: game.rating
                        onConfirm(game.copy(title = title, genre = genre, description = description, rating = parsedRating, imageResId = selectedImageResId))
                    }, colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary), enabled = title.isNotBlank() && genre.isNotBlank()) {
                        Text("Сохранить", color = Color.White)
                    }
                }
            }
        }
    }

    if (showImageSelector) {
        ImageSelectorDialog(
            selectedImageResId = selectedImageResId,
            onImageSelected = { selectedImageResId = it; showImageSelector = false },
            onDismiss = { showImageSelector = false }
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = SteamColors.TextPrimary,
    unfocusedTextColor = SteamColors.TextPrimary,
    focusedLabelColor = SteamColors.Primary,
    unfocusedLabelColor = SteamColors.TextSecondary,
    focusedBorderColor = SteamColors.Primary,
    unfocusedBorderColor = SteamColors.Border
)