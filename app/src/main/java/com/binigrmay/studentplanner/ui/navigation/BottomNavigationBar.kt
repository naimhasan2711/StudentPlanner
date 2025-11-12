package com.binigrmay.studentplanner.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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