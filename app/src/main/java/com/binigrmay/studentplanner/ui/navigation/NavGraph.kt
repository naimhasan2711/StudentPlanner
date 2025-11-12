package com.binigrmay.studentplanner.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
