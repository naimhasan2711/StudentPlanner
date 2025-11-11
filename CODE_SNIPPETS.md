# Code Snippets Reference 📝

Quick reference for common tasks in StudentPlanner development.

## Table of Contents
1. [Database Operations](#database-operations)
2. [ViewModel Usage](#viewmodel-usage)
3. [Compose UI](#compose-ui)
4. [Navigation](#navigation)
5. [Notifications](#notifications)
6. [Date & Time](#date--time)
7. [Forms & Validation](#forms--validation)

## Database Operations

### Insert a Task
```kotlin
viewModelScope.launch {
    val task = Task(
        title = "Study for Exam",
        description = "Chapters 1-5",
        dueDate = System.currentTimeMillis() + 86400000,
        priority = Priority.HIGH,
        category = "Exam Prep"
    )
    repository.insertTask(task)
}
```

### Update a Task
```kotlin
viewModelScope.launch {
    val updatedTask = task.copy(isCompleted = true)
    repository.updateTask(updatedTask)
}
```

### Delete a Task
```kotlin
viewModelScope.launch {
    repository.deleteTask(task)
}
```

### Query Tasks
```kotlin
// Get today's tasks
repository.getTasksForDay(startOfDay, endOfDay)
    .collect { tasks ->
        // Handle tasks
    }

// Get tasks by priority
repository.getTasksByPriority(Priority.HIGH)
    .collect { tasks ->
        // Handle high priority tasks
    }
```

## ViewModel Usage

### Observe State in Composable
```kotlin
@Composable
fun MyScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.incompleteTasks.collectAsState()
    
    LazyColumn {
        items(tasks) { task ->
            TaskCard(task = task)
        }
    }
}
```

### Handle UI Events
```kotlin
// In ViewModel
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

fun loadData() {
    viewModelScope.launch {
        _uiState.value = UiState.Loading
        try {
            val data = repository.getData()
            _uiState.value = UiState.Success(data)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message)
        }
    }
}

// In Composable
val uiState by viewModel.uiState.collectAsState()

when (val state = uiState) {
    is UiState.Loading -> LoadingIndicator()
    is UiState.Success -> ContentView(state.data)
    is UiState.Error -> ErrorView(state.message)
}
```

## Compose UI

### Basic Card with Click
```kotlin
@Composable
fun ItemCard(
    item: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
```

### Dialog
```kotlin
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
```

### Text Field with State
```kotlin
@Composable
fun TaskInputField() {
    var text by remember { mutableStateOf("") }
    
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Task Title") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
```

### Dropdown Menu
```kotlin
@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedPriority.name)
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}
```

### Date Picker
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )
    
    OutlinedButton(onClick = { showDialog = true }) {
        Text(SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            .format(Date(selectedDate)))
    }
    
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
```

### Loading Indicator
```kotlin
@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
```

### Empty State
```kotlin
@Composable
fun EmptyState(
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAction) {
                Text(actionText)
            }
        }
    }
}
```

## Navigation

### Navigate to Screen
```kotlin
// In composable
val navController = rememberNavController()

Button(onClick = {
    navController.navigate(Screen.Tasks.route)
}) {
    Text("Go to Tasks")
}
```

### Navigate with Arguments
```kotlin
// Navigate
navController.navigate(Screen.EditTask.createRoute(taskId))

// In NavHost
composable(
    route = Screen.EditTask.route,
    arguments = listOf(navArgument("taskId") { type = NavType.IntType })
) { backStackEntry ->
    val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
    EditTaskScreen(taskId = taskId)
}
```

### Pop Back Stack
```kotlin
Button(onClick = { navController.popBackStack() }) {
    Text("Go Back")
}
```

## Notifications

### Show Notification
```kotlin
NotificationHelper.showTaskNotification(
    context = context,
    taskId = task.id,
    title = task.title,
    description = task.description
)
```

### Schedule Reminder
```kotlin
ReminderScheduler.scheduleTaskReminder(context, task)
```

### Cancel Reminder
```kotlin
ReminderScheduler.cancelTaskReminder(context, taskId)
```

## Date & Time

### Get Current Time
```kotlin
val now = System.currentTimeMillis()
```

### Format Date
```kotlin
val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
val formatted = dateFormat.format(Date(timestamp))
```

### Start/End of Day
```kotlin
val startOfDay = DateTimeUtils.getStartOfDay()
val endOfDay = DateTimeUtils.getEndOfDay()
```

### Calculate Due Date
```kotlin
// Tomorrow
val tomorrow = System.currentTimeMillis() + 86400000

// In 7 days
val nextWeek = System.currentTimeMillis() + (7 * 86400000)

// Specific time
val calendar = Calendar.getInstance()
calendar.set(2024, Calendar.DECEMBER, 25, 9, 0, 0)
val dueDate = calendar.timeInMillis
```

## Forms & Validation

### Form State
```kotlin
@Composable
fun TaskForm(onSubmit: (Task) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        
        Button(onClick = {
            val task = Task(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = selectedPriority
            )
            onSubmit(task)
        }) {
            Text("Save")
        }
    }
}
```

### Validation
```kotlin
fun validateTask(
    title: String,
    dueDate: Long
): ValidationResult {
    return when {
        title.isBlank() -> ValidationResult.Error("Title is required")
        title.length < 3 -> ValidationResult.Error("Title too short")
        dueDate < System.currentTimeMillis() -> 
            ValidationResult.Error("Due date must be in future")
        else -> ValidationResult.Success
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
```

### Show Validation Error
```kotlin
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
```

## Advanced Patterns

### Search with Debounce
```kotlin
// In ViewModel
private val _searchQuery = MutableStateFlow("")
val searchQuery: StateFlow<String> = _searchQuery

val searchResults: StateFlow<List<Task>> = _searchQuery
    .debounce(300) // Wait 300ms after user stops typing
    .flatMapLatest { query ->
        if (query.isBlank()) {
            flowOf(emptyList())
        } else {
            repository.searchTasks(query)
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

fun updateSearchQuery(query: String) {
    _searchQuery.value = query
}
```

### Pagination
```kotlin
// In ViewModel
private val pageSize = 20
private var currentPage = 0

fun loadMore() {
    viewModelScope.launch {
        val offset = currentPage * pageSize
        val newItems = repository.getTasks(limit = pageSize, offset = offset)
        _tasks.value = _tasks.value + newItems
        currentPage++
    }
}
```

### Pull to Refresh
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableList(viewModel: TaskViewModel) {
    val tasks by viewModel.allTasks.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.refreshTasks()
            isRefreshing = false
        }
    )
    
    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn {
            items(tasks) { task ->
                TaskCard(task = task)
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
```

## Testing Snippets

### ViewModel Test
```kotlin
@Test
fun `insert task updates task list`() = runTest {
    val task = Task(title = "Test Task", dueDate = System.currentTimeMillis())
    viewModel.insertTask(task)
    
    val tasks = viewModel.allTasks.first()
    assertTrue(tasks.contains(task))
}
```

### Composable Test
```kotlin
@Test
fun taskCard_displaysTitle() {
    composeTestRule.setContent {
        TaskCard(
            task = Task(title = "Test Task", dueDate = 0),
            onCheckedChange = {},
            onClick = {}
        )
    }
    
    composeTestRule.onNodeWithText("Test Task").assertExists()
}
```

---

## Tips

1. **Always handle null safely**: Use `?.let {}` or Elvis operator `?:`
2. **Use remember for UI state**: Prevents recreation on recomposition
3. **Use LaunchedEffect for side effects**: API calls, navigation
4. **Collect Flow in composables**: Use `.collectAsState()`
5. **Keep ViewModels clean**: Business logic only, no UI code

## Common Mistakes to Avoid

❌ **Don't** use `GlobalScope.launch`
✅ **Do** use `viewModelScope.launch`

❌ **Don't** call suspend functions directly in composables
✅ **Do** use `LaunchedEffect` or ViewModels

❌ **Don't** modify state directly in composables
✅ **Do** use ViewModels and state hoisting

❌ **Don't** pass ViewModels deeply
✅ **Do** use `hiltViewModel()` at screen level

---

**Quick Reference Created! Copy these snippets as needed.** 📋✨
