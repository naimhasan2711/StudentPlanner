package com.binigrmay.studentplanner.ui.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Today : Screen("today")
    object Tasks : Screen("tasks")
    object Calendar : Screen("calendar")
    object Settings : Screen("settings")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
    object AddLecture : Screen("add_lecture")
    object EditLecture : Screen("edit_lecture/{lectureId}") {
        fun createRoute(lectureId: Int) = "edit_lecture/$lectureId"
    }
}

/**
 * Bottom navigation items
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: String
) {
    object Today : BottomNavItem(
        route = Screen.Today.route,
        title = "Today",
        icon = "today"
    )
    
    object Tasks : BottomNavItem(
        route = Screen.Tasks.route,
        title = "Tasks",
        icon = "task"
    )
    
    object Calendar : BottomNavItem(
        route = Screen.Calendar.route,
        title = "Calendar",
        icon = "calendar"
    )
    
    object Settings : BottomNavItem(
        route = Screen.Settings.route,
        title = "Settings",
        icon = "settings"
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Today,
    BottomNavItem.Tasks,
    BottomNavItem.Calendar,
    BottomNavItem.Settings
)
