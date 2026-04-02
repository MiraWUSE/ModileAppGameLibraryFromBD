# Game Library

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)

Мобильное приложение для управления библиотекой игр в стиле Steam.

## Структура проекта

```
GameLibrary/
├── com.example.gamelibrary/
│   ├── data/
│   │   ├── local/
│   │   │   ├── entity/
│   │   │   │   ├── GameEntity.kt    # Сущность игры
│   │   │   │   └── Profile.kt       # Сущность профиля
│   │   │   ├── dao/
│   │   │   │   ├── GameDao.kt       # DAO для игр
│   │   │   │   └── ProfileDao.kt    # DAO для профиля
│   │   │   ├── AppDatabase.kt       # Конфигурация БД
│   │   │   └── MockData.kt          # Неиспользуемые захардкожентые игры 
│   │   └── repository/              # Репозитории
│   │            ├── GameRepository.kt       # Repository для игр
│   │            └── ProfileRepository.kt    # Repository для профиля
│   ├── ui/
│   │   ├── theme/
│   │   │   └── SteamColors.kt       # Цветовая схема Steam
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt        # Главная + добавление игр
│   │   │   ├── LibraryScreen.kt     # Библиотека + редактирование
│   │   │   ├── DetailScreen.kt      # Детали игры + удаление
│   │   │   ├── ProfileScreen.kt     # Профиль пользователя
│   │   │   └── SearchScreen.kt      # Поиск игр
│   │   └── viewmodel/               # ViewModel
│   │           ├── GameViewModel.kt       # ViewModel для игр
│   │           ├── ProfileViewModel.kt       # ViewModel для профиля
│   │           └── ViewModelFactory.kt    # ViewModel для логики
│   ├── utils/
│   │   └── GameImages.kt            # Управление картинками
│   ├── navigation/
│   │   ├── NavGraph.kt              # Граф навигации
│   │   └── Screens.kt    	     # Просто окна
│   ├── MainActivity.kt              # Точка входа
│   └── GameLibraryApp.kt            # Главный Composable
└── build.gradle.kts                 # Зависимости
```


## Функциональность
### 🏠 Главная (HomeScreen)
#### Список популярных игр из БД
- ➕ Добавление новой игры с выбором обложки
- 🔍 Переход к поиску
- 🗑️ Удаление игры (долгое нажатие)
- 📚 Библиотека (LibraryScreen)
#### 2) Сетка игр (2 колонки)
- ✏️ Редактирование: название, жанр, описание, рейтинг, обложка
- 🗑️ Удаление игры
- 🎮 Детали игры (DetailScreen)
#### 3) Полная информация об игре
- 🗑️ Красная кнопка удаления внизу
- 🔍 Поиск (SearchScreen)
#### 4) Поиск по названию и жанру
#### 5) 👤 Профиль (ProfileScreen)
- Отображение данных пользователя из БД
- ✏️ Редактирование: имя, фамилия, информация о себе
- 📊 Прогресс заданий и статистика

## Особенности UI

### Steam-стиль
```
object SteamColors {
    val Background = Color(0xFF171A21)   // Тёмный фон
    val Surface = Color(0xFF1B2838)      // Карточки
    val Primary = Color(0xFF66C0F4)      // Акценты
    val TextPrimary = Color(0xFFFFFFFF)  // Белый текст
    val Rating = Color(0xFFA4D007)       // Зелёный рейтинг
}
```

### 🖼️ Динамические обложки
- 12 встроенных изображений
- Случайный выбор при создании
- Возможность смены при редактировании


