package com.example.gamelibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.utils.getRandomGameImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onGameClick: (Int) -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }

    // Получаем результаты поиска из БД
    val searchResults by gameViewModel.searchGames(query).collectAsState(initial = emptyList())

    // Задержка для поиска (чтобы не искать на каждый символ)
    var delayedQuery by remember { mutableStateOf("") }

    // Debounce - задержка поиска на 300мс
    LaunchedEffect(query) {
        kotlinx.coroutines.delay(300)
        delayedQuery = query
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SteamColors.Background)
    ) {
        // Верхняя панель с поиском
        Surface(
            color = SteamColors.Surface,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Назад",
                                tint = SteamColors.TextPrimary
                            )
                        }

                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = {
                                Text("Поиск игр...", color = SteamColors.TextSecondary)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Поиск",
                                    tint = SteamColors.Primary
                                )
                            },
                            trailingIcon = {
                                if (query.isNotBlank()) {
                                    IconButton(onClick = { query = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Очистить",
                                            tint = SteamColors.TextSecondary
                                        )
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SteamColors.TextPrimary,
                                unfocusedTextColor = SteamColors.TextPrimary,
                                focusedPlaceholderColor = SteamColors.TextSecondary,
                                unfocusedPlaceholderColor = SteamColors.TextSecondary,
                                focusedLabelColor = SteamColors.Primary,
                                unfocusedLabelColor = SteamColors.TextSecondary,
                                focusedBorderColor = SteamColors.Primary,
                                unfocusedBorderColor = SteamColors.Border,
                                cursorColor = SteamColors.Primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Количество результатов
                Text(
                    text = if (delayedQuery.isNotBlank()) {
                        "Найдено: ${searchResults.size}"
                    } else {
                        "Введите название игры"
                    },
                    fontSize = 14.sp,
                    color = SteamColors.TextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        // Результаты поиска
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = SteamColors.TextSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (query.isBlank()) {
                        Text(
                            text = "Введите название игры\nдля поиска",
                            fontSize = 16.sp,
                            color = SteamColors.TextSecondary,
                            lineHeight = 24.sp
                        )
                    } else {
                        Text(
                            text = "Ничего не найдено\nпо запросу \"$query\"",
                            fontSize = 16.sp,
                            color = SteamColors.TextSecondary,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { query = "" },
                            colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary)
                        ) {
                            Text("Очистить поиск", color = Color.White)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchResults) { game ->
                    SearchGameCard(
                        game = game,
                        onClick = { onGameClick(game.id) }
                    )
                }
            }
        }
    }
}

//  Карточка игры в поиске
@Composable
fun SearchGameCard(
    game: GameEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
            // Изображение игры
            Image(
                painter = painterResource(
                    id = game.imageResId.takeIf { it != 0 } ?: getRandomGameImage()
                ),
                contentDescription = game.title,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация об игре
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = game.genre,
                    fontSize = 14.sp,
                    color = SteamColors.TextSecondary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Краткое описание
                Text(
                    text = game.description,
                    fontSize = 13.sp,
                    color = SteamColors.TextSecondary.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Рейтинг
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Рейтинг",
                    tint = SteamColors.Rating,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = game.rating.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.Rating
                )
            }
        }
    }
}