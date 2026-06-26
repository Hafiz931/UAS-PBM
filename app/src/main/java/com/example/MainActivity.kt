package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    val repository = (application as TaskBinApplication).repository
    
    setContent {
      MyApplicationTheme {
        val viewModel: TaskBinViewModel = viewModel(factory = TaskBinViewModelFactory(repository))
        MainScreen(viewModel)
      }
    }
  }
}

@Composable
fun MainScreen(viewModel: TaskBinViewModel) {
  val navController = rememberNavController()
  
    val items = listOf(
    NavigationItem("Beranda", "dashboard", Icons.Default.Home),
    NavigationItem("Tugas", "tasks", Icons.AutoMirrored.Filled.List),
    NavigationItem("Burnout", "burnout", Icons.Default.Favorite),
    NavigationItem("Pustaka", "publicbin", Icons.Default.Share),
    NavigationItem("Fokus", "focus", Icons.Default.Timer)
  )

  Scaffold(
    bottomBar = {
      NavigationBar(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
          NavigationBarItem(
            icon = { Icon(screen.icon, contentDescription = screen.name) },
            label = { Text(screen.name) },
            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
            onClick = {
              navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                  saveState = true
                }
                launchSingleTop = true
                restoreState = true
              }
            }
          )
        }
      }
    }
  ) { innerPadding ->
    NavHost(navController, startDestination = "dashboard", Modifier.padding(innerPadding)) {
      composable("dashboard") { DashboardScreen(viewModel, onNavigateToTasks = { navController.navigate("tasks") }) }
      composable("tasks") { MyTasksScreen(viewModel, onAddTask = { navController.navigate("add_task") }) }
      composable("burnout") { BurnoutScreen(viewModel) }
      composable("publicbin") { PublicBinScreen(viewModel) }
      composable("focus") { FocusModeScreen(viewModel) }
      composable("add_task") { AddTaskScreen(viewModel, onBack = { navController.popBackStack() }) }
    }
  }
}

data class NavigationItem(val name: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
