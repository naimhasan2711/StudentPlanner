# StudentPlanner 📚

A modern, feature-rich Android application designed specifically for students to efficiently manage their academic life. Built with the latest Android technologies including Jetpack Compose, Material Design 3, and MVVM architecture, StudentPlanner helps you stay organized with tasks, lectures, and deadlines all in one beautiful interface.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com)
[![Material Design 3](https://img.shields.io/badge/Material%20Design-3-blue)](https://m3.material.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📱 Screenshots

> Add screenshots of your app here showcasing different screens and features

---

## ✨ Key Features

### 🏠 Today View
- **Smart Dashboard**: View all today's tasks and lectures at a glance
- **Elegant Header**: Displays current date with a beautiful calendar badge
- **Quick Stats**: Live count badges for tasks and lectures
- **Quick Actions**: Bottom sheet for quick task/lecture creation
- **Color-Coded Cards**: Visual distinction with gradient indicators

### ✅ Task Management
- **Comprehensive Task Details**:
  - Title and description
  - **Due date AND time** (with time picker)
  - Priority levels (Low, Medium, High, Urgent)
  - Custom categories
  - Completion status
- **Smart Reminders**: Customizable notification alerts before deadlines
- **Visual Priority System**: Color-coded priority badges with gradients
- **Enhanced Task Cards**:
  - 4dp elevation with shadow
  - 1dp colored border based on priority
  - Vertical gradient indicator bar (4dp)
  - Calendar and clock icons for date/time
  - Circular delete button
  - Category badges
- **Task Actions**:
  - Mark as complete/incomplete
  - Edit task details
  - Delete with confirmation dialog
  - Quick access from Today, Tasks, and Calendar screens

### 📖 Lecture Schedule Management
- **Detailed Lecture Information**:
  - Title, instructor, and room number
  - Day of week selection
  - Start and end time
  - Color coding for easy identification
  - Optional notes
- **Recurring Lectures**: Weekly recurring schedule support
- **Reminder System**: Configurable alerts (15, 30, 60, 120 minutes before)
- **Premium Lecture Cards**:
  - 6dp elevation with gradient border
  - Vertical gradient accent bar (5dp)
  - Elegant time section with shadow
  - Information cards for instructor/room
  - Weekly and day badges
  - Delete button with confirmation
- **Smart Default**: Auto-selects current day when adding lectures

### 📅 Weekly Calendar View
- **Day Selector**: Dropdown to view lectures for any day
- **Smart Default**: Automatically shows current day's schedule
- **Lecture Organization**: All lectures sorted by time
- **Quick Actions**: Tap to edit, delete with confirmation
- **Empty States**: Friendly messages when no lectures scheduled

### 🎨 Beautiful Modern UI
- **Material Design 3**: Latest design guidelines implementation
- **Theme Support**: Light and Dark mode
- **Smooth Animations**: Polished transitions and interactions
- **Responsive Layout**: Adapts to different screen sizes
- **Premium Styling**:
  - Gradient backgrounds and borders
  - Shadow elevations
  - Rounded corners (8-20dp)
  - Custom typography (Medium, SemiBold, Bold, ExtraBold)
  - Icon-enhanced metadata display

### 🔔 Smart Notifications
- **Background Scheduling**: Uses WorkManager for reliability
- **Exact Time Alarms**: Precise notifications at scheduled times
- **Persistent**: Survives app closure and device reboots
- **Customizable Timing**: Choose when to be reminded
- **Rich Notifications**: Includes task/lecture details

### ⚙️ Settings & Customization
- **Theme Control**: Toggle between Light, Dark, and System default
- **Notification Preferences**: Manage app notifications
- **About Section**: App information and version

---

## 🏗️ Technical Architecture

### Architecture Pattern: MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                          │
│  (Jetpack Compose Screens & Components)                 │
│  - TodayScreen                                          │
│  - TasksScreen, AddTaskScreen, EditTaskScreen          │
│  - CalendarScreen                                       │
│  - AddLectureScreen, EditLectureScreen                 │
│  - SettingsScreen                                       │
│  - TaskCard, LectureCard Components                    │
└────────────────┬────────────────────────────────────────┘
                 │ observes StateFlow/Flow
                 ↓
┌─────────────────────────────────────────────────────────┐
│                    ViewModel Layer                       │
│  - TaskViewModel                                        │
│  - LectureViewModel                                     │
│  - SettingsViewModel                                    │
│  (Business Logic, State Management)                     │
└────────────────┬────────────────────────────────────────┘
                 │ calls methods
                 ↓
┌─────────────────────────────────────────────────────────┐
│                   Repository Layer                       │
│  - TaskRepository                                       │
│  - LectureRepository                                    │
│  (Single Source of Truth, Data Abstraction)            │
└────────────────┬────────────────────────────────────────┘
                 │ uses
                 ↓
┌─────────────────────────────────────────────────────────┐
│                     Data Layer                           │
│  ┌─────────────────────────────────────────────────┐   │
│  │  Room Database (SQLite)                         │   │
│  │  - TaskDao                                      │   │
│  │  - LectureDao                                   │   │
│  │  - Converters                                   │   │
│  │  - AppDatabase (v2 with migrations)            │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### Data Flow

```
User Interaction → Composable Screen → ViewModel → Repository → DAO → Database
                                 ↑                                        ↓
                        StateFlow/Flow ← Flow ← Flow ← Query Result ←────┘
```

---

## 🛠️ Tech Stack & Dependencies

### Core Technologies

| Technology | Purpose | Version |
|-----------|---------|---------|
| **Kotlin** | Programming Language | 2.0.21 |
| **Jetpack Compose** | Modern UI Toolkit | Latest |
| **Material 3** | Design System | Latest |
| **Coroutines** | Async Programming | Latest |
| **Flow** | Reactive Streams | Latest |
| **Hilt** | Dependency Injection | Latest |
| **Room** | Local Database | Latest |
| **Navigation Compose** | Navigation | Latest |
| **WorkManager** | Background Tasks | Latest |
| **DataStore** | Key-Value Storage | Latest |

### Key Libraries

```kotlin
dependencies {
    // Compose & Material 3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose")
    
    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    
    // Hilt Dependency Injection
    implementation("com.google.dagger:hilt-android")
    kapt("com.google.dagger:hilt-compiler")
    implementation("androidx.hilt:hilt-navigation-compose")
    implementation("androidx.hilt:hilt-work")
    
    // Room Database
    implementation("androidx.room:room-runtime")
    implementation("androidx.room:room-ktx")
    kapt("androidx.room:room-compiler")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences")
    
    // Material Icons
    implementation("androidx.compose.material:material-icons-extended")
}
```

---

## 📁 Detailed Project Structure

```
com.binigrmay.studentplanner/
│
├── 📂 data/                              # Data Layer
│   ├── 📂 db/                            # Database
│   │   ├── AppDatabase.kt                # Room database (v2)
│   │   ├── Converters.kt                 # Type converters (Priority, DayOfWeek)
│   │   ├── TaskDao.kt                    # Task queries & operations
│   │   └── LectureDao.kt                 # Lecture queries & operations
│   │
│   ├── 📂 model/                         # Data Models
│   │   ├── Task.kt                       # Task entity with dueTime field
│   │   ├── Lecture.kt                    # Lecture entity
│   │   ├── Priority.kt                   # Priority enum (LOW, MEDIUM, HIGH, URGENT)
│   │   └── DayOfWeek.kt                  # Day enum with Calendar converter
│   │
│   └── 📂 repository/                    # Repository Pattern
│       ├── TaskRepository.kt             # Task data operations
│       └── LectureRepository.kt          # Lecture data operations
│
├── 📂 di/                                # Dependency Injection
│   └── DatabaseModule.kt                 # Hilt modules (Database, DAOs, Repositories)
│
├── 📂 ui/                                # Presentation Layer
│   ├── 📂 components/                    # Reusable Components
│   │   ├── TaskCard.kt                   # Premium task card with delete
│   │   └── LectureCard.kt                # Premium lecture card with delete
│   │
│   ├── 📂 navigation/                    # Navigation Setup
│   │   ├── Screen.kt                     # Screen routes
│   │   └── NavGraph.kt                   # Navigation graph
│   │
│   ├── 📂 screens/                       # Screen Composables
│   │   ├── 📂 today/
│   │   │   └── TodayScreen.kt            # Dashboard with stats badges
│   │   ├── 📂 tasks/
│   │   │   ├── TasksScreen.kt            # All tasks with filters
│   │   │   ├── AddTaskScreen.kt          # Add task with time picker
│   │   │   └── EditTaskScreen.kt         # Edit task with time picker
│   │   ├── 📂 lectures/
│   │   │   ├── AddLectureScreen.kt       # Add lecture (current day default)
│   │   │   └── EditLectureScreen.kt      # Edit lecture
│   │   ├── 📂 calendar/
│   │   │   └── CalendarScreen.kt         # Weekly view (current day default)
│   │   └── 📂 settings/
│   │       └── SettingsScreen.kt         # Theme & preferences
│   │
│   └── 📂 theme/                         # Material 3 Theming
│       ├── Color.kt                      # Color schemes
│       ├── Theme.kt                      # Theme composition
│       └── Type.kt                       # Typography scale
│
├── 📂 viewmodel/                         # ViewModels
│   ├── TaskViewModel.kt                  # Task state & operations
│   ├── LectureViewModel.kt               # Lecture state & operations
│   └── SettingsViewModel.kt              # Settings state
│
├── 📂 utils/                             # Utility Classes
│   ├── NotificationHelper.kt             # Notification management
│   ├── ReminderScheduler.kt              # Schedule alarms with exact time
│   └── DateTimeUtils.kt                  # Date/time formatting helpers
│
├── MainActivity.kt                        # Single Activity
└── StudentPlannerApplication.kt          # Application class (Hilt)
```

---

## 💾 Database Schema

### Database Version: 2
- **Migration 1→2**: Added `dueTime` field to tasks table

### Task Table Schema

```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,                    // Task title
    val description: String = "",          // Optional description
    val dueDate: Long,                     // Due date (timestamp)
    val dueTime: String = "23:59",        // Due time (HH:mm format)
    val priority: Priority = Priority.MEDIUM,  // LOW, MEDIUM, HIGH, URGENT
    val isCompleted: Boolean = false,      // Completion status
    val reminderEnabled: Boolean = false,  // Reminder toggle
    val reminderTime: Long? = null,        // Reminder time (minutes before)
    val createdAt: Long = System.currentTimeMillis(),
    val category: String = "General"       // Task category
)
```

**Indexes**: Primary key on `id`

### Lecture Table Schema

```kotlin
@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,                     // Lecture title
    val instructor: String = "",           // Professor/teacher name
    val room: String = "",                 // Room number/location
    val dayOfWeek: DayOfWeek,             // MONDAY, TUESDAY, etc.
    val startTime: String,                 // Start time (HH:mm)
    val endTime: String,                   // End time (HH:mm)
    val isRecurring: Boolean = true,       // Weekly recurring
    val specificDate: Long? = null,        // For one-time lectures
    val color: String = "#6200EE",        // Hex color code
    val notes: String = "",                // Additional notes
    val reminderEnabled: Boolean = false,  // Reminder toggle
    val reminderMinutesBefore: Int = 15   // Reminder timing
)
```

**Indexes**: Primary key on `id`

### Type Converters

```kotlin
class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name
    
    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
    
    @TypeConverter
    fun fromDayOfWeek(dayOfWeek: DayOfWeek): String = dayOfWeek.name
    
    @TypeConverter
    fun toDayOfWeek(value: String): DayOfWeek = DayOfWeek.valueOf(value)
}
```

---

## 🎨 UI/UX Design Details

### Material Design 3 Components

#### Enhanced TaskCard
- **Elevation**: 4dp (default) → 8dp (pressed)
- **Border**: 1dp with priority-based color
- **Gradient Indicator**: 4dp vertical bar (left side)
- **Typography**: SemiBold for title, Medium for metadata
- **Interactive Elements**:
  - Checkbox for completion
  - Circular delete button (error color)
  - Full card click for editing
- **Metadata Display**:
  - Priority badge with border and gradient
  - Date with calendar icon
  - Time with clock icon
  - Category badge (separate row to prevent cutoff)

#### Premium LectureCard
- **Elevation**: 6dp (default) → 10dp (pressed)
- **Border**: 1dp gradient border
- **Gradient Bar**: 5dp vertical accent
- **Time Section**: Premium design with shadow and gradient background
- **Information Cards**: Separate cards for instructor and room
- **Badges**: Weekly, Day, Notification badges
- **Delete Button**: Circular button with confirmation dialog
- **Color Coding**: Each lecture has customizable color

### Color System

```kotlin
// Light Theme
val PrimaryLight = Color(0xFF6200EE)
val SecondaryLight = Color(0xFF03DAC5)
val TertiaryLight = Color(0xFF018786)
val ErrorLight = Color(0xFFB00020)

// Dark Theme  
val PrimaryDark = Color(0xFFBB86FC)
val SecondaryDark = Color(0xFF03DAC6)
val TertiaryDark = Color(0xFF03DAC6)
val ErrorDark = Color(0xFFCF6679)

// Priority Colors
val PriorityLow = Color(0xFF4CAF50)      // Green
val PriorityMedium = Color(0xFFFF9800)   // Orange
val PriorityHigh = Color(0xFFFF5722)     // Deep Orange
val PriorityUrgent = Color(0xFFD32F2F)   // Red
```

### Typography Scale

```kotlin
// Material 3 Typography
headlineLarge: 32.sp, Bold
headlineMedium: 28.sp, Bold
headlineSmall: 24.sp, Bold
titleLarge: 22.sp, SemiBold
titleMedium: 16.sp, SemiBold
titleSmall: 14.sp, Medium
bodyLarge: 16.sp, Regular
bodyMedium: 14.sp, Regular
bodySmall: 12.sp, Regular
labelLarge: 14.sp, Bold
labelMedium: 12.sp, Medium
labelSmall: 11.sp, Medium
```

### Spacing & Sizing

```kotlin
// Elevation
Surface: 0.dp
Card: 4-6.dp
FAB: 6.dp
Dialog: 24.dp

// Corner Radius
Small: 8.dp
Medium: 12-16.dp
Large: 20.dp

// Padding
Small: 8.dp
Medium: 16.dp
Large: 24.dp

// Icon Sizes
Small: 14-16.dp
Medium: 20-24.dp
Large: 32-48.dp
```

---

## 🔔 Notification System

### Implementation Architecture

```
User Action → ViewModel → ReminderScheduler
                              ↓
                    WorkManager (OneTimeWorkRequest)
                              ↓
                    TaskReminderWorker / LectureReminderWorker
                              ↓
                    NotificationHelper
                              ↓
                    Android NotificationManager
                              ↓
                    User Notification
```

### Reminder Scheduling

#### For Tasks
```kotlin
fun scheduleTaskReminder(context: Context, task: Task) {
    // Combines dueDate + dueTime for exact timestamp
    val calendar = Calendar.getInstance().apply {
        timeInMillis = task.dueDate
        // Parse dueTime (HH:mm)
        val timeParts = task.dueTime.split(":")
        set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        set(Calendar.MINUTE, timeParts[1].toInt())
    }
    
    // Calculate reminder time
    val reminderTime = calendar.timeInMillis - (task.reminderTime ?: 0)
    
    // Schedule WorkManager task
    val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("taskId" to task.id))
        .build()
    
    WorkManager.getInstance(context).enqueueUniqueWork(
        "task_reminder_${task.id}",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}
```

#### For Lectures
```kotlin
fun scheduleLectureReminder(context: Context, lecture: Lecture) {
    // Calculate next occurrence of lecture
    val lectureTime = getNextLectureTime(lecture.dayOfWeek, lecture.startTime)
    val reminderTime = lectureTime - (lecture.reminderMinutesBefore * 60 * 1000)
    
    // Schedule recurring reminder for weekly lectures
    // ...similar to task reminder
}
```

### Notification Features
- **Rich Content**: Title, description, time
- **Actions**: Mark as done, Snooze (future enhancement)
- **Channels**: Separate channels for tasks and lectures
- **Priority**: High priority for urgent tasks
- **Sound & Vibration**: Configurable
- **Exact Timing**: Uses SCHEDULE_EXACT_ALARM permission

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Version 11 or higher
- **Minimum SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 36 (Android 15)
- **Kotlin**: 2.0.21 or later
- **Gradle**: 8.9 or later

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/naimhasan2711/StudentPlanner.git
   cd StudentPlanner
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select `File → Open`
   - Navigate to the cloned directory
   - Click `OK`

3. **Sync Project with Gradle Files**
   - Android Studio should automatically trigger a Gradle sync
   - If not, click `File → Sync Project with Gradle Files`
   - Wait for dependencies to download

4. **Build the Project**
   ```bash
   ./gradlew build
   ```

5. **Run on Emulator or Device**
   - **Emulator**: 
     - Open AVD Manager (`Tools → Device Manager`)
     - Create/Start an Android Virtual Device (API 26+)
   - **Physical Device**:
     - Enable Developer Options and USB Debugging
     - Connect via USB
   
   - Click the **Run** button (▶️) or press `Shift + F10`
   - Select your device/emulator
   - App will install and launch

### Verify Installation

After installation, you should see:
- ✅ Bottom navigation with 4 tabs (Today, Tasks, Calendar, Settings)
- ✅ Today screen with current date and empty state
- ✅ Ability to create tasks and lectures
- ✅ Theme toggle in Settings

---

## ⚙️ Configuration & Customization

### 1. Change App Name

Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your Custom App Name</string>
```

### 2. Change Package Name

**Method 1: Android Studio Refactor**
1. Right-click on package in Project view
2. Select `Refactor → Rename`
3. Choose "Rename package"
4. Enter new package name
5. Click "Refactor"

**Method 2: Manual**
1. Update `applicationId` in `app/build.gradle.kts`:
   ```kotlin
   defaultConfig {
       applicationId = "com.yourcompany.yourapp"
   }
   ```
2. Update package in `AndroidManifest.xml`
3. Refactor all Kotlin files

### 3. Customize Theme Colors

Edit `ui/theme/Color.kt`:
```kotlin
// Light theme colors
val PrimaryLight = Color(0xFF6200EE)  // Your primary color
val SecondaryLight = Color(0xFF03DAC5)  // Your secondary color

// Dark theme colors
val PrimaryDark = Color(0xFFBB86FC)
val SecondaryDark = Color(0xFF03DAC6)
```

### 4. Modify Database

When changing data models:
1. Update the entity class
2. Increment database version:
   ```kotlin
   @Database(
       entities = [Task::class, Lecture::class],
       version = 3  // Increment this
   )
   ```
3. Create migration:
   ```kotlin
   val MIGRATION_2_3 = object : Migration(2, 3) {
       override fun migrate(database: SupportSQLiteDatabase) {
           // Add your migration SQL
       }
   }
   ```
4. Add migration to database builder

### 5. Configure Notifications

Edit `utils/NotificationHelper.kt`:
```kotlin
// Notification channel settings
private const val CHANNEL_NAME = "Your Channel Name"
private const val CHANNEL_DESCRIPTION = "Your description"
```

### 6. Adjust Build Configuration

Edit `app/build.gradle.kts`:
```kotlin
android {
    compileSdk = 36  // Latest Android SDK
    
    defaultConfig {
        minSdk = 26  // Minimum supported Android version
        targetSdk = 36
        versionCode = 1  // Increment for each release
        versionName = "1.0.0"  // Semantic versioning
    }
}
```

---

## 📱 Permissions

The app requires the following permissions (declared in `AndroidManifest.xml`):

### Required Permissions

```xml
<!-- Notifications (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

<!-- Exact Alarms for precise reminders -->
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.USE_EXACT_ALARM"/>

<!-- Restart alarms after device reboot -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

<!-- Internet (for future features like cloud sync) -->
<uses-permission android:name="android.permission.INTERNET"/>
```

### Permission Handling

- **Runtime Permissions**: Automatically requested for notifications on Android 13+
- **Exact Alarms**: Declared in manifest, user grants via system settings
- **Boot Completed**: For rescheduling reminders after device restart

---

## 🧪 Testing

### Unit Tests

Run unit tests for ViewModels, Repositories, and Utils:
```bash
./gradlew test
```

Test files location: `app/src/test/java/`

### Instrumentation Tests

Run UI and integration tests:
```bash
./gradlew connectedAndroidTest
```

Test files location: `app/src/androidTest/java/`

### Manual Testing Checklist

- [ ] Create a task with all fields
- [ ] Create a recurring lecture
- [ ] Mark task as complete
- [ ] Delete task/lecture with confirmation
- [ ] Edit task/lecture
- [ ] Toggle theme in settings
- [ ] Verify reminders appear at correct time
- [ ] Test on different screen sizes
- [ ] Test on both light and dark themes
- [ ] Verify database persistence after app restart

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Build Fails with "Cannot resolve symbol"
**Solution**: 
- `File → Invalidate Caches → Invalidate and Restart`
- `./gradlew clean build`

#### 2. Compose Preview Not Showing
**Solution**:
- Enable "Build & Refresh" in preview
- Check if `@Preview` annotation is present
- Rebuild project

#### 3. Hilt Dependency Injection Errors
**Solution**:
- Ensure `@HiltAndroidApp` on Application class
- Add `@AndroidEntryPoint` to Activity
- Check if all dependencies are properly annotated

#### 4. Room Database Migration Errors
**Solution**:
- Clear app data: `Settings → Apps → StudentPlanner → Clear Data`
- Or uninstall and reinstall app (development only)
- Ensure migration is properly defined

#### 5. Notifications Not Appearing
**Solution**:
- Check notification permission granted
- Verify "Do Not Disturb" is off
- Check notification channel is enabled
- For Android 12+, ensure exact alarm permission

---

## 📈 Performance Optimization

### Implemented Optimizations

1. **Lazy Loading**: LazyColumn for efficient list rendering
2. **State Hoisting**: Proper state management in Compose
3. **Database Indexing**: Primary keys on all tables
4. **Coroutines**: Non-blocking async operations
5. **Flow**: Reactive data streams for live updates
6. **WorkManager**: Efficient background task scheduling
7. **Remember**: Cached composable values
8. **Keys**: Stable keys for list items

### Best Practices

- Use `derivedStateOf` for computed values
- Avoid unnecessary recompositions
- Use `LaunchedEffect` for side effects
- Keep ViewModels thin, move logic to repositories
- Use stable collections in state

---

## 🔮 Future Enhancements

### Planned Features

- [ ] **Statistics Dashboard**
  - Task completion graphs
  - Study time tracking
  - Weekly/monthly reports
  
- [ ] **Cloud Sync**
  - Firebase integration
  - Cross-device synchronization
  - Backup and restore

- [ ] **Widgets**
  - Today's tasks widget
  - Upcoming lectures widget
  - Quick add widget

- [ ] **Collaboration**
  - Share tasks with classmates
  - Group study sessions
  - Shared lecture notes

- [ ] **Advanced Features**
  - Pomodoro timer
  - Study planner
  - Grade tracker
  - Calendar integration (Google Calendar)
  - PDF attachments
  - Voice notes
  - OCR for handwritten notes

- [ ] **Export/Import**
  - Export to CSV/JSON
  - Print schedule
  - Share calendar

- [ ] **Customization**
  - Custom themes
  - Accent color picker
  - Font size options
  - Layout preferences

---

## 🤝 Contributing

Contributions are welcome and appreciated! Here's how you can help:

### How to Contribute

1. **Fork the Repository**
   ```bash
   # Click "Fork" on GitHub
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. **Make Your Changes**
   - Write clean, documented code
   - Follow Kotlin coding conventions
   - Add comments for complex logic
   - Update README if needed

4. **Test Your Changes**
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

5. **Commit Your Changes**
   ```bash
   git commit -m "Add some AmazingFeature"
   ```

6. **Push to Your Fork**
   ```bash
   git push origin feature/AmazingFeature
   ```

7. **Open a Pull Request**
   - Go to original repository
   - Click "New Pull Request"
   - Describe your changes
   - Link related issues

### Contribution Guidelines

- **Code Style**: Follow Kotlin coding conventions
- **Commits**: Use meaningful commit messages
- **Documentation**: Update docs for new features
- **Tests**: Add tests for new functionality
- **Issues**: Link PRs to related issues
- **Review**: Be open to feedback and suggestions

### Areas for Contribution

- 🐛 Bug fixes
- ✨ New features
- 📝 Documentation improvements
- 🎨 UI/UX enhancements
- ⚡ Performance optimizations
- 🧪 Test coverage
- 🌍 Translations/Localization

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 StudentPlanner

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👨‍💻 Developer

**Naim Hasan**
- GitHub: [@naimhasan2711](https://github.com/naimhasan2711)

Built with ❤️ for students everywhere to make academic life more organized and stress-free.

---

## 🙏 Acknowledgments

Special thanks to:

- **Google Android Team** for amazing tools and libraries
- **Material Design Team** for comprehensive design guidelines
- **Jetpack Compose Team** for revolutionizing Android UI development
- **Kotlin Team** for the beautiful programming language
- **Open Source Community** for inspiration and learning resources

### Resources & References

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [WorkManager Documentation](https://developer.android.com/topic/libraries/architecture/workmanager)

---

## 📞 Support

If you encounter any issues or have questions:

1. **Check Documentation**: Review this README thoroughly
2. **Search Issues**: Look through existing GitHub issues
3. **Create Issue**: Open a new issue with:
   - Clear description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots (if applicable)
   - Device/Android version info

---

## ⭐ Star This Repository

If you find this project helpful, please consider giving it a ⭐️ on GitHub!

---

<div align="center">

**Happy Planning! 📚✨**

*Making student life organized, one task at a time.*

</div>
