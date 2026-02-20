package com.showcase.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.showcase.app.ui.addtask.AddTaskScreen
import com.showcase.app.ui.addtask.AddTaskViewModel
import com.showcase.app.ui.taskdetail.TaskDetailScreen
import com.showcase.app.ui.taskdetail.TaskDetailViewModel
import com.showcase.app.ui.tasklist.TaskListScreen
import com.showcase.app.ui.tasklist.TaskListViewModel

@Composable
fun ShowcaseNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.TASK_LIST
    ) {
        composable(NavRoutes.TASK_LIST) {
            val viewModel: TaskListViewModel = hiltViewModel()
            TaskListScreen(
                viewModel = viewModel,
                onAddTask = { navController.navigate(NavRoutes.ADD_TASK) },
                onTaskClick = { id -> navController.navigate(NavRoutes.taskDetail(id)) }
            )
        }
        composable(
            route = NavRoutes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) {
            val viewModel: TaskDetailViewModel = hiltViewModel()
            TaskDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onDeleted = { navController.popBackStack(NavRoutes.TASK_LIST, inclusive = false) }
            )
        }
        composable(NavRoutes.ADD_TASK) {
            val viewModel: AddTaskViewModel = hiltViewModel()
            AddTaskScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
