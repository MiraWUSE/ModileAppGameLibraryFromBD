package com.example.gamelibrary.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamelibrary.data.local.entity.GameEntity
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.GameViewModel
import com.example.gamelibrary.utils.getRandomGameImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    gameId: Int,
    onBack: () -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    // Получаем игру из БД по ID
    val game by gameViewModel.getGameById(gameId).collectAsState(initial = null)

    // Диалог подтверждения удаления
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить игру", color = SteamColors.TextPrimary) },
            text = {
                Text(
                    "Вы точно хотите удалить \"${game?.title}\"?",
                    color = SteamColors.TextSecondary
                )
            },
            containerColor = SteamColors.Surface,
            confirmButton = {
                Button(
                    onClick = {
                        game?.let { gameViewModel.deleteGame(it) }
                        showDeleteDialog = false
                        onBack()  // Возвращаемся назад после удаления
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) {
                    Text("Удалить", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SteamColors.Primary)
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    // Если игра не найдена — показываем заглушку
    if (game == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SteamColors.Background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Игра не найдена", color = SteamColors.TextSecondary, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary)) {
                Text("Назад", color = Color.White)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game!!.title, color = SteamColors.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = SteamColors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SteamColors.Surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SteamColors.Background)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Изображение игры
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SteamColors.SurfaceLight)
            ) {
                Image(
                    painter = painterResource(
                        id = game!!.imageResId.takeIf { it != 0 } ?: getRandomGameImage()
                    ),
                    contentDescription = game!!.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Название игры
            Text(
                text = game!!.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = SteamColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Жанр в плашке
            Surface(
                color = SteamColors.SurfaceLight,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = game!!.genre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SteamColors.Primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Рейтинг
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(SteamColors.Surface, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Campaign,
                    contentDescription = "Рейтинг",
                    tint = SteamColors.Rating,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = game!!.rating.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SteamColors.Rating
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Описание
            Card(
                colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Описание",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SteamColors.Primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = game!!.description,
                        fontSize = 15.sp,
                        color = SteamColors.TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),  // 🔴 Красный цвет
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Удалить игру",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}