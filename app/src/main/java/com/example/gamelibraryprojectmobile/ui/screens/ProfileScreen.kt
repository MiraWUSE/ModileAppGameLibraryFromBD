package com.example.gamelibrary.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gamelibrary.R
import com.example.gamelibrary.navigation.Screen
import com.example.gamelibrary.data.local.entity.Profile
import com.example.gamelibrary.ui.theme.SteamColors
import com.example.gamelibrary.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Состояние для 5 галочек
    val tasks = remember { mutableStateListOf(false, false, false, false, false) }
    val completedCount = tasks.count { it }
    val progress = completedCount / 5f
    val isRewardUnlocked = progress >= 1f

    // Получаем профиль из БД
    val profiles by profileViewModel.allProfiles.collectAsState(initial = emptyList())
    val currentProfile = profiles.firstOrNull()

    // Состояния для редактирования
    var firstName by remember { mutableStateOf(currentProfile?.firstName ?: "") }
    var lastName by remember { mutableStateOf(currentProfile?.lastName ?: "") }
    var personalInfo by remember { mutableStateOf(currentProfile?.personalInfo ?: "") }

    // Диалог подтверждения выхода
    if (showDialog) {
        SteamAlertDialog(
            title = "Выход",
            text = "Вы точно хотите выйти из профиля?",
            onConfirm = {
                showDialog = false
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
            onDismiss = { showDialog = false }
        )
    }

    // Диалог редактирования профиля
    if (showEditDialog) {
        EditProfileDialog(
            firstName = firstName,
            lastName = lastName,
            personalInfo = personalInfo,
            onFirstNameChange = { firstName = it },
            onLastNameChange = { lastName = it },
            onPersonalInfoChange = { personalInfo = it },
            onConfirm = {
                // Сохраняем изменения в БД
                val updatedProfile = currentProfile?.copy(
                    firstName = firstName,
                    lastName = lastName,
                    personalInfo = personalInfo
                ) ?: Profile(
                    firstName = firstName,
                    lastName = lastName,
                    personalInfo = personalInfo
                )
                profileViewModel.insertProfile(updatedProfile) // INSERT OR REPLACE
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SteamColors.Background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Аватарка + кнопка редактирования
        Box {
            Image(
                painter = painterResource(id = R.drawable.eye),
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // Кнопка редактирования поверх аватара
            IconButton(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(SteamColors.Primary, CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Имя и фамилия из БД
        Text(
            text = "${currentProfile?.firstName ?: "Danilka"} ${currentProfile?.lastName ?: ""}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = SteamColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Личная информация из БД
        Text(
            text = currentProfile?.personalInfo ?: "Доп. инфа • Крутышка я",
            fontSize = 16.sp,
            color = SteamColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ШКАЛА ПРОГРЕССА
        Card(
            colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Крутая награда", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary)
                    Text("$completedCount/5", fontSize = 16.sp, color = SteamColors.Primary)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(8.dp)),
                    color = SteamColors.Primary,
                    trackColor = SteamColors.SurfaceLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Для получения подарка выполните все задания", fontSize = 14.sp, color = SteamColors.TextSecondary)
                Text("${(progress * 100).toInt()}% выполнено", fontSize = 14.sp, color = SteamColors.TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5 ГАЛОЧЕК
        Text("Задания", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tasks.indices.toList()) { index ->
                TaskCheckbox(
                    isCompleted = tasks[index],
                    onToggle = { tasks[index] = !tasks[index] }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Награда при 100%
        AnimatedVisibility(visible = isRewardUnlocked, enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.8f)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Color(0xFFFFC107)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Твоя награда!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SteamColors.Primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Все задания выполнены!", fontSize = 16.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.tuma),
                        contentDescription = "Награда",
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Статистика
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatCard("Игр", currentProfile?.gamesPlayed?.toString() ?: "100")
            StatCard("Уровень", currentProfile?.level?.toString() ?: "5")
            StatCard("Достижений", "10k+")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка выхода
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Выйти из профиля", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// 🎨 Компонент диалога редактирования профиля
@Composable
fun EditProfileDialog(
    firstName: String,
    lastName: String,
    personalInfo: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPersonalInfoChange: (String) -> Unit,
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
                Text("Редактировать профиль", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SteamColors.TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = onFirstNameChange,
                    label = { Text("Имя", color = SteamColors.TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SteamColors.TextPrimary,
                        unfocusedTextColor = SteamColors.TextPrimary,
                        focusedLabelColor = SteamColors.Primary,
                        unfocusedLabelColor = SteamColors.TextSecondary,
                        focusedBorderColor = SteamColors.Primary,
                        unfocusedBorderColor = SteamColors.Border
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = onLastNameChange,
                    label = { Text("Фамилия", color = SteamColors.TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SteamColors.TextPrimary,
                        unfocusedTextColor = SteamColors.TextPrimary,
                        focusedLabelColor = SteamColors.Primary,
                        unfocusedLabelColor = SteamColors.TextSecondary,
                        focusedBorderColor = SteamColors.Primary,
                        unfocusedBorderColor = SteamColors.Border
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = personalInfo,
                    onValueChange = onPersonalInfoChange,
                    label = { Text("О себе", color = SteamColors.TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SteamColors.TextPrimary,
                        unfocusedTextColor = SteamColors.TextPrimary,
                        focusedLabelColor = SteamColors.Primary,
                        unfocusedLabelColor = SteamColors.TextSecondary,
                        focusedBorderColor = SteamColors.Primary,
                        unfocusedBorderColor = SteamColors.Border
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = SteamColors.TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary)
                    ) {
                        Text("Сохранить", color = Color.White)
                    }
                }
            }
        }
    }
}

// 🎨 Компонент диалога подтверждения
@Composable
fun SteamAlertDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = SteamColors.TextPrimary) },
        text = { Text(text, color = SteamColors.TextSecondary) },
        containerColor = SteamColors.Surface,
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = SteamColors.Primary)) {
                Text("Да", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, colors = ButtonDefaults.outlinedButtonColors(contentColor = SteamColors.Primary)) {
                Text("Нет")
            }
        }
    )
}

@Composable
fun TaskCheckbox(isCompleted: Boolean, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp).clickable { onToggle() }) {
        Icon(
            imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Circle,
            contentDescription = if (isCompleted) "Выполнено" else "Не выполнено",
            tint = if (isCompleted) Color(0xFF4CAF50) else Color(0xFFBDBDBD),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("Задание", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteamColors.Surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(100.dp).height(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = SteamColors.Primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 14.sp, color = SteamColors.TextSecondary)
        }
    }
}