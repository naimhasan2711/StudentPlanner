# StudentPlanner 📚

A comprehensive Android app for students to manage their lectures, deadlines, and tasks in one organized place.

## 🎯 Features

### Core Functionality
- **Today View**: See all tasks and lectures scheduled for today
- **Task Management**: Create, edit, mark as complete, and delete tasks with:
  - Title and description
  - Due date and time
  - Priority levels (Low, Medium, High, Urgent)
  - Categories
  - Reminder notifications
- **Lecture Schedule**: Manage recurring and one-time lectures with:
  - Title, instructor, and room information
  - Weekly recurring schedule
  - Custom time slots
  - Color coding for easy identification
  - Reminder notifications
- **Weekly Calendar**: View your weekly schedule of lectures
- **Settings**: Toggle between light and dark themes

### Advanced Features
- **Notifications**: Get reminded before tasks and lectures
- **Priority System**: Color-coded priority badges for tasks
- **Search & Filter**: Find tasks by priority, category, or completion status
- **Material 3 Design**: Modern, beautiful UI with smooth animations

## 🧩 Tech Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt (Dagger)
- **Database**: Room (SQLite)
- **Async Operations**: Kotlin Coroutines & Flow
- **Navigation**: Navigation Compose
- **Background Work**: WorkManager
- **Settings Storage**: DataStore Preferences
- **Logging**: Timber

### Key Libraries
```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose")

// Room Database
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
kapt("androidx.room:room-compiler")

// Hilt Dependency Injection
implementation("com.google.dagger:hilt-android")
kapt("com.google.dagger:hilt-compiler")
implementation("androidx.hilt:hilt-navigation-compose")

// WorkManager
implementation("androidx.work:work-runtime-ktx")
implementation("androidx.hilt:hilt-work")

// DataStore
implementation("androidx.datastore:datastore-preferences")
```

## 📁 Project Structure

```
com.binigrmay.studentplanner
│
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt          # Room database configuration
│   │   ├── Converters.kt           # Type converters for Room
│   │   ├── TaskDao.kt              # Task data access object
│   │   └── LectureDao.kt           # Lecture data access object
│   │
│   ├── model/
│   │   ├── Task.kt                 # Task entity
│   │   ├── Lecture.kt              # Lecture entity
│   │   └── Priority.kt             # Priority enum
│   │
│   └── repository/
│       ├── TaskRepository.kt       # Task repository
│       └── LectureRepository.kt    # Lecture repository
│
├── di/
│   └── DatabaseModule.kt           # Hilt dependency injection modules
│
├── ui/
│   ├── components/
│   │   ├── TaskCard.kt             # Reusable task card component
│   │   └── LectureCard.kt          # Reusable lecture card component
│   │
│   ├── navigation/
│   │   ├── Screen.kt               # Navigation routes
│   │   └── NavGraph.kt             # Navigation graph setup
│   │
│   ├── screens/
│   │   ├── today/
│   │   │   └── TodayScreen.kt      # Today view screen
│   │   ├── tasks/
│   │   │   └── TasksScreen.kt      # Tasks management screen
│   │   ├── calendar/
│   │   │   └── CalendarScreen.kt   # Weekly calendar screen
│   │   └── settings/
│   │       └── SettingsScreen.kt   # Settings screen
│   │
│   └── theme/
│       ├── Color.kt                # App colors
│       ├── Theme.kt                # Material 3 theme setup
│       └── Type.kt                 # Typography
│
├── viewmodel/
│   ├── TaskViewModel.kt            # Task view model
│   ├── LectureViewModel.kt         # Lecture view model
│   └── SettingsViewModel.kt        # Settings view model
│
├── worker/
│   ├── TaskReminderWorker.kt       # Task notification worker
│   └── LectureReminderWorker.kt    # Lecture notification worker
│
├── utils/
│   ├── NotificationHelper.kt       # Notification management
│   └── ReminderScheduler.kt        # Schedule reminders
│
├── MainActivity.kt                  # Main activity
└── StudentPlannerApplication.kt    # Application class
```

## 🏗️ Architecture

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:

### Layers

1. **UI Layer (View)**
   - Jetpack Compose screens and components
   - Observes StateFlow from ViewModels
   - Handles user interactions

2. **ViewModel Layer**
   - Manages UI state
   - Handles business logic
   - Communicates with repositories
   - Uses Kotlin Flow for reactive data

3. **Repository Layer**
   - Abstracts data sources
   - Provides clean API for ViewModels
   - Single source of truth

4. **Data Layer**
   - Room database for local persistence
   - DAOs for database operations
   - Entities define data structure

### Data Flow

```
UI → ViewModel → Repository → DAO → Database
 ↑                                      ↓
 ← StateFlow/Flow ← Flow ← Flow ← Query Result
```

## 🎨 UI Components

### Material 3 Design
- **Color Schemes**: Light and dark theme support
- **Typography**: Clear hierarchy with Material 3 type scale
- **Components**: Cards, buttons, FABs, bottom navigation
- **Animations**: Smooth transitions and interactions

### Custom Components
- **TaskCard**: Displays task with priority badge, due date, and completion checkbox
- **LectureCard**: Shows lecture with color indicator, time, instructor, and room
- **PriorityBadge**: Color-coded badges for task priorities

## 🔔 Notifications

### WorkManager Integration
- Background task scheduling for reminders
- Persists across device reboots
- Efficient battery usage

### Notification Types
1. **Task Reminders**: Notifies before task deadlines
2. **Lecture Reminders**: Alerts before lectures start

### Implementation
```kotlin
// Schedule a task reminder
ReminderScheduler.scheduleTaskReminder(context, task)

// Cancel a reminder
ReminderScheduler.cancelTaskReminder(context, taskId)
```

## 💾 Database Schema

### Task Table
```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Priority,
    val isCompleted: Boolean,
    val reminderEnabled: Boolean,
    val reminderTime: Long?,
    val createdAt: Long,
    val category: String
)
```

### Lecture Table
```kotlin
@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val instructor: String,
    val room: String,
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String,
    val isRecurring: Boolean,
    val specificDate: Long?,
    val color: String,
    val notes: String,
    val reminderEnabled: Boolean,
    val reminderMinutesBefore: Int
)
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 36
- Kotlin 2.0.21 or later

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/studentplanner.git
   cd studentplanner
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press Shift+F10

## 🔧 Configuration

### Changing App Name
Update `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### Changing Package Name
1. Refactor package name in Android Studio
2. Update `applicationId` in `app/build.gradle.kts`
3. Update package in `AndroidManifest.xml`

### Customizing Colors
Edit `ui/theme/Color.kt` to change app colors:
```kotlin
val PrimaryLight = Color(0xFF6200EE)
val PrimaryDark = Color(0xFFBB86FC)
```

## 📱 Permissions

The app requires the following permissions:

- `POST_NOTIFICATIONS`: Show reminder notifications (Android 13+)
- `SCHEDULE_EXACT_ALARM`: Schedule exact time alarms for reminders
- `USE_EXACT_ALARM`: Use exact alarm API
- `RECEIVE_BOOT_COMPLETED`: Reschedule reminders after device reboot

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## 🎯 Future Enhancements

- [ ] Task completion statistics and graphs
- [ ] Export/import data
- [ ] Cloud sync with Firebase
- [ ] Widget for home screen
- [ ] More theme options
- [ ] Task attachments
- [ ] Collaboration features
- [ ] Calendar integration
- [ ] Study timer/Pomodoro

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👨‍💻 Developer

Built with ❤️ for students everywhere

## 🙏 Acknowledgments

- Material Design 3 guidelines
- Android Jetpack documentation
- Kotlin Flow best practices
- MVVM architecture pattern

---

**Happy Planning! 📚✨**
