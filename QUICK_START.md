# Quick Start Guide - StudentPlanner 🚀

## Build & Run (5 Minutes)

### Prerequisites Check
```bash
# Verify you have:
✓ Android Studio Hedgehog or later
✓ JDK 11 or later
✓ Android SDK with API 26-36
```

### Step 1: Open Project
1. Launch Android Studio
2. Select "Open" → Navigate to project folder
3. Wait for Gradle sync to complete

### Step 2: Run the App
1. Connect Android device or start emulator (API 26+)
2. Click ▶️ Run button (or Shift+F10)
3. App should launch in ~30 seconds

## First Time Setup

### Grant Permissions
On first launch (Android 13+):
- Tap "Allow" for notification permission
- Needed for task/lecture reminders

### Test the App
1. **Today Screen**: See empty state
2. **Tasks Tab**: Navigate to tasks view
3. **Calendar Tab**: Check weekly schedule
4. **Settings Tab**: Toggle dark theme

## Quick Test Scenarios

### Test 1: Add a Task Programmatically
Since Add/Edit screens aren't implemented yet, you can test by adding sample data:

```kotlin
// In TodayScreen or TasksScreen, add a test button:
Button(onClick = {
    val task = Task(
        title = "Sample Assignment",
        description = "Test task description",
        dueDate = System.currentTimeMillis() + 86400000, // Tomorrow
        priority = Priority.HIGH,
        category = "Homework"
    )
    taskViewModel.insertTask(task)
}) {
    Text("Add Test Task")
}
```

### Test 2: Add a Lecture Programmatically
```kotlin
Button(onClick = {
    val lecture = Lecture(
        title = "Data Structures",
        instructor = "Dr. Smith",
        room = "Room 301",
        dayOfWeek = DayOfWeek.MONDAY,
        startTime = "09:00",
        endTime = "10:30",
        color = "#6200EE"
    )
    lectureViewModel.insertLecture(lecture)
}) {
    Text("Add Test Lecture")
}
```

## Project Structure Overview

```
app/src/main/java/com/binigrmay/studentplanner/
├── data/              # Database, DAOs, Repositories
├── di/                # Dependency Injection (Hilt)
├── ui/                # Screens, Components, Theme
├── viewmodel/         # ViewModels
├── worker/            # Background Workers
├── utils/             # Helper functions
└── MainActivity.kt    # Entry point
```

## Key Files to Know

| File | Purpose |
|------|---------|
| `Task.kt` | Task data model |
| `Lecture.kt` | Lecture data model |
| `TaskViewModel.kt` | Task business logic |
| `LectureViewModel.kt` | Lecture business logic |
| `TodayScreen.kt` | Main dashboard |
| `AppDatabase.kt` | Room database |

## Common Commands

### Build
```bash
./gradlew assembleDebug
```

### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

### Run Tests
```bash
./gradlew test
```

### Check Dependencies
```bash
./gradlew dependencies
```

## Troubleshooting

### "Unresolved reference" errors
```bash
# Sync Gradle
File → Sync Project with Gradle Files

# Or invalidate caches
File → Invalidate Caches → Invalidate and Restart
```

### Build fails
```bash
# Clean and rebuild
./gradlew clean
./gradlew build --refresh-dependencies
```

### Emulator issues
```bash
# Wipe emulator data
Tools → AVD Manager → Wipe Data

# Or create new emulator
Tools → AVD Manager → Create Virtual Device
```

## Development Workflow

### 1. Make Changes
Edit Kotlin files in Android Studio

### 2. Hot Reload (Compose)
- Just save - changes appear instantly
- No need to rebuild for UI changes

### 3. Full Rebuild
```bash
Build → Clean Project
Build → Rebuild Project
```

### 4. Run & Test
Click ▶️ Run

## What to Build Next

### Priority 1: Add/Edit Screens
Create these dialogs/screens:

1. **Add Task Dialog**
```kotlin
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    // Form fields for title, description, etc.
    // Date picker for due date
    // Priority selector
    // Save button calls onSave
}
```

2. **Add Lecture Dialog**
```kotlin
@Composable
fun AddLectureDialog(
    onDismiss: () -> Unit,
    onSave: (Lecture) -> Unit
) {
    // Form fields
    // Day selector
    // Time pickers
    // Color picker
}
```

### Priority 2: Form Validation
Add validation helpers:
```kotlin
fun validateTask(task: Task): ValidationResult {
    return when {
        task.title.isBlank() -> ValidationResult.Error("Title required")
        task.dueDate < System.currentTimeMillis() -> 
            ValidationResult.Error("Date must be in future")
        else -> ValidationResult.Success
    }
}
```

### Priority 3: Enhanced UI
- Swipe to delete gestures
- Pull to refresh
- Search bars
- Filter chips

## Useful Code Snippets

### Show Toast
```kotlin
val context = LocalContext.current
LaunchedEffect(Unit) {
    Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT).show()
}
```

### Date Picker
```kotlin
val datePickerState = rememberDatePickerState()
DatePicker(state = datePickerState)
```

### Confirmation Dialog
```kotlin
AlertDialog(
    onDismissRequest = { /* dismiss */ },
    title = { Text("Confirm") },
    text = { Text("Are you sure?") },
    confirmButton = {
        TextButton(onClick = { /* confirm */ }) {
            Text("Yes")
        }
    },
    dismissButton = {
        TextButton(onClick = { /* dismiss */ }) {
            Text("No")
        }
    }
)
```

## Resources

### Documentation
- [README.md](./README.md) - Full project documentation
- [USAGE_GUIDE.md](./USAGE_GUIDE.md) - User guide
- [DEV_SUMMARY.md](./DEV_SUMMARY.md) - Development details

### Official Docs
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)

### Community
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android-jetpack-compose)
- [Reddit r/androiddev](https://reddit.com/r/androiddev)
- [Kotlin Slack](https://kotlinlang.org/community/)

## Need Help?

### Check These First
1. ✅ Gradle sync completed?
2. ✅ No compilation errors?
3. ✅ Correct API level?
4. ✅ Device/emulator running?

### Still Stuck?
1. Read error message carefully
2. Google the error
3. Check Stack Overflow
4. Review documentation
5. Ask in community forums

## Success Checklist

After following this guide, you should be able to:
- ✅ Build the project successfully
- ✅ Run the app on device/emulator
- ✅ Navigate between screens
- ✅ Toggle dark/light theme
- ✅ View empty states
- ✅ Understand project structure
- ✅ Know what to build next

## Next Steps

1. ✅ App is running? Great!
2. → Read [README.md](./README.md) for architecture details
3. → Check [DEV_SUMMARY.md](./DEV_SUMMARY.md) for what's implemented
4. → Start building Add/Edit screens
5. → Test thoroughly
6. → Deploy! 🚀

---

**Happy Coding! 💻✨**

*Questions? Check the docs or open an issue on GitHub.*
