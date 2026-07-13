package com.evgenii.githubviewer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.evgenii.githubviewer.ui.auth.AuthScreen
import com.evgenii.githubviewer.ui.auth.AuthViewModel
import com.evgenii.githubviewer.ui.detail.DetailInfoScreen
import com.evgenii.githubviewer.ui.detail.RepositoryInfoViewModel
import com.evgenii.githubviewer.ui.list.RepositoriesListScreen
import com.evgenii.githubviewer.ui.list.RepositoriesListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object Routes {
    const val AUTH = "auth"
    const val LIST = "list"
    const val DETAIL = "detail/{repoId}"

    fun detail(repoId: String) = "detail/$repoId"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.AUTH) {
            val viewModel: AuthViewModel = koinViewModel()
            AuthScreen(
                viewModel = viewModel,
                onNavigateToMain = {
                    navController.navigate(Routes.LIST) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LIST) {
            val viewModel: RepositoriesListViewModel = koinViewModel()
            RepositoriesListScreen(
                viewModel = viewModel,
                onRepoClick = { repo ->
                    navController.navigate(Routes.detail(repo.name))
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("repoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val repoId = backStackEntry.arguments?.getString("repoId") ?: return@composable
            val viewModel: RepositoryInfoViewModel = koinViewModel(
                parameters = { parametersOf(repoId) }
            )
            DetailInfoScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}