package com.binigrmay.studentplanner.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.binigrmay.studentplanner.ui.screens.calendar.CalendarScreen
import com.binigrmay.studentplanner.ui.screens.lectures.AddLectureScreen
import com.binigrmay.studentplanner.ui.screens.lectures.EditLectureScreen
import com.binigrmay.studentplanner.ui.screens.settings.SettingsScreen
import com.binigrmay.studentplanner.ui.screens.tasks.AddTaskScreen
import com.binigrmay.studentplanner.ui.screens.tasks.EditTaskScreen
import com.binigrmay.studentplanner.ui.screens.tasks.TasksScreen
import com.binigrmay.studentplanner.ui.screens.today.TodayScreen
import com.binigrmay.studentplanner.viewmodel.LectureViewModel
import com.binigrmay.studentplanner.viewmodel.SettingsViewModel
import com.binigrmay.studentplanner.viewmodel.TaskViewModel

/**
 * Main navigation graph for the app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    taskViewModel: TaskViewModel = hiltViewModel(),
    lectureViewModel: LectureViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Today.route) {
                TodayScreen(
                    taskViewModel = taskViewModel,
                    lectureViewModel = lectureViewModel,
                    onNavigateToAddTask = {
                        navController.navigate(Screen.AddTask.route)
                    },
                    onNavigateToAddLecture = {
                        navController.navigate(Screen.AddLecture.route)
                    }
                )
            }
            
            composable(Screen.Tasks.route) {
                TasksScreen(
                    viewModel = taskViewModel,
                    onNavigateToAddTask = {
                        navController.navigate(Screen.AddTask.route)
                    },
                    onNavigateToEditTask = { taskId ->
                        navController.navigate(Screen.EditTask.createRoute(taskId))
                    }
                )
            }
            
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    taskViewModel = taskViewModel,
                    lectureViewModel = lectureViewModel,
                    onNavigateToAddTask = {
                        navController.navigate(Screen.AddTask.route)
                    },
                    onNavigateToAddLecture = {
                        navController.navigate(Screen.AddLecture.route)
                    }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel
                )
            }
            
            // Add Task Screen
            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    taskViewModel = taskViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Add Lecture Screen
            composable(Screen.AddLecture.route) {
                AddLectureScreen(
                    lectureViewModel = lectureViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Edit Task Screen
            composable(
                route = Screen.EditTask.route,
                arguments = listOf(navArgument("taskId") { type = NavType.IntType })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
                EditTaskScreen(
                    taskId = taskId,
                    taskViewModel = taskViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Edit Lecture Screen
            composable(
                route = Screen.EditLecture.route,
                arguments = listOf(navArgument("lectureId") { type = NavType.IntType })
            ) { backStackEntry ->
                val lectureId = backStackEntry.arguments?.getInt("lectureId") ?: 0
                EditLectureScreen(
                    lectureId = lectureId,
                    lectureViewModel = lectureViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * Bottom navigation bar
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = getIconForRoute(item.route),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Get the appropriate icon for each route
 */
private fun getIconForRoute(route: String): ImageVector {
    return when (route) {
        Screen.Today.route -> Icons.Filled.Home
        Screen.Tasks.route -> Icons.Filled.CheckCircle
        Screen.Calendar.route -> Icons.Filled.DateRange
        Screen.Settings.route -> Icons.Filled.Settings
        else -> Icons.Filled.Home
    }
}
