package com.example.gamelibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamelibrary.R
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.utils.gameImageResources
import com.example.gamelibrary.utils.getRandomGameImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGameClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newGameTitle by remember { mutableStateOf("") }
    var newGameGenre by remember { mutableStateOf("") }
    var newGameDescription by remember { mutableStateOf("") }
    var newGameRating by remember { mutableStateOf("5.0") }
    var selectedImageResId by remember { mutableStateOf(getRandomGameImage()) }
    var showImageSelector by remember { mutableStateOf(false) }

    val games by gameViewModel.allGames.collectAsState(initial = emptyList())

    if (showAddDialog) {
        AddGameDialog(
            title = newGameTitle,
            genre = newGameGenre,
            description = newGameDescription,
            rating = newGameRating,
            selectedImageResId = selectedImageResId,
            showImageSelector = showImageSelector,
            onTitleChange = { newGameTitle = it },
            onGenreChange = { newGameGenre = it },
            onDescriptionChange = { newGameDescription = it },
            onRatingChange = { newGameRating = it },
            onImageChange = { selectedImageResId = it },
            onShowImageSelectorChange = { showImageSelector = it },
            onConfirm = {
                val parsedRating = newGameRating.toFloatOrNull()?.coerceIn(0f, 5f) ?: 5.0f
                val newGame = GameEntity(
                    title = newGameTitle,
                    genre = newGameGenre,
                    description = newGameDescription,
                    rating = parsedRating,
                    imageResId = selectedImageResId
                )
                gameViewModel.insertGame(newGame)
                newGameTitle = ""
                newGameGenre = ""
                newGameDescription = ""
                newGameRating = "5.0"
                selectedImageResId = getRandomGameImage()
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SteamColors.Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SteamColors.Surface)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Магазин игр",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary
                )
                Text(
                    text = "Покупайте игры)",
                    fontSize = 16.sp,
                    color = SteamColors.TextSecondary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SteamColors.Primary,
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить игру"
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Card(
                    onClick = onSearchClick,
                    colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Поиск",
                            tint = SteamColors.Primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Поиск игр...",
                            fontSize = 16.sp,
                            color = SteamColors.TextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Популярное",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(games) { game ->
                GameCard(
                    game = game,
                    onClick = { onGameClick(game.id) },
                    onDelete = { gameViewModel.deleteGame(game) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun GameCard(
    game: GameEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onDelete
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = game.imageResId.takeIf { it != 0 } ?: getRandomGameImage()),
                contentDescription = game.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = game.genre,
                    fontSize = 14.sp,
                    color = SteamColors.TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Рейтинг",
                        tint = SteamColors.Rating,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = game.rating.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SteamColors.Rating
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color(0xFFEF5350),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameDialog(
    title: String,
    genre: String,
    description: String,
    rating: String,
    selectedImageResId: Int,
    showImageSelector: Boolean,
    onTitleChange: (String) -> Unit,
    onGenreChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onRatingChange: (String) -> Unit,
    onImageChange: (Int) -> Unit,
    onShowImageSelectorChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Добавить игру",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Обложка:",
                        fontSize = 14.sp,
                        color = SteamColors.TextSecondary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = selectedImageResId),
                            contentDescription = "Обложка",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { onShowImageSelectorChange(true) }) {
                            Text("Выбрать", color = SteamColors.Primary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Название", color = SteamColors.TextSecondary) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = genre,
                    onValueChange = onGenreChange,
                    label = { Text("Жанр", color = SteamColors.TextSecondary) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Описание", color = SteamColors.TextSecondary) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = rating,
                    onValueChange = onRatingChange,
                    label = { Text("Рейтинг (0-5)", color = SteamColors.TextSecondary) },
                    colors = textFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = SteamColors.TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary),
                        enabled = title.isNotBlank() && genre.isNotBlank()
                    ) {
                        Text("Добавить", color = Color.White)
                    }
                }
            }
        }
    }

    if (showImageSelector) {
        ImageSelectorDialog(
            selectedImageResId = selectedImageResId,
            onImageSelected = { onImageChange(it); onShowImageSelectorChange(false) },
            onDismiss = { onShowImageSelectorChange(false) }
        )
    }
}

@Composable
fun ImageSelectorDialog(
    selectedImageResId: Int,
    onImageSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Выберите обложку",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gameImageResources) { resId ->
                        Card(
                            onClick = { onImageSelected(resId) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (resId == selectedImageResId) SteamColors.Primary else SteamColors.SurfaceLight
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Обложка",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SteamColors.SurfaceLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена", color = SteamColors.TextPrimary)
                }
            }
        }
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