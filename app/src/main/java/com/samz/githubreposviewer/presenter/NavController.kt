package com.samz.githubreposviewer.presenter

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.samz.githubreposviewer.presenter.details.RepositoryDetailsScreen
import com.samz.githubreposviewer.presenter.issues.RepositoryIssuesScreen
import com.samz.githubreposviewer.presenter.list.MainViewModel
import com.samz.githubreposviewer.presenter.list.RepositoriesScreen
import com.samz.githubreposviewer.presenter.list.RepositoriesViewModel
import com.samz.githubreposviewer.theme.GithubReposViewerTheme

sealed class Screen(val name: String) {
    object RepositoryListScreen : Screen("RepositoriesScreen")
    object RepositoryDetailsScreen : Screen("RepositoryDetailsScreen")
    object RepositoryIssuesScreen : Screen("RepositoryIssuesScreen")
}

@Composable
fun NavController() {
    val navController = rememberNavController()
    val themeChangeViewModel: MainViewModel = hiltViewModel()

    GithubReposViewerTheme(appTheme = themeChangeViewModel.stateApp.theme) {
        NavHost(
            navController = navController,
            startDestination = Screen.RepositoryListScreen.name
        ) {
            composable(
                route = Screen.RepositoryListScreen.name,
            ) {
                val viewModel: RepositoriesViewModel = hiltViewModel()
                RepositoriesScreen(viewModel, themeChangeViewModel) { user: String, repo: String ->
                    navController.navigate("${Screen.RepositoryDetailsScreen.name}/$user/$repo")
                }
            }
            composable(
                route = "${Screen.RepositoryDetailsScreen.name}/{user}/{repo}",
                arguments = listOf(
                    navArgument("user") { type = NavType.StringType },
                    navArgument("repo") { type = NavType.StringType }
                ),
            ) {
                RepositoryDetailsScreen(
                    it.arguments?.getString("user"),
                    it.arguments?.getString("repo"),
                    navigate = { user: String, repo: String ->
                        navController.navigate("${Screen.RepositoryIssuesScreen.name}/$user/$repo")
                    },
                    popBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "${Screen.RepositoryIssuesScreen.name}/{user}/{repo}",
                arguments = listOf(
                    navArgument("user") { type = NavType.StringType },
                    navArgument("repo") { type = NavType.StringType }
                ),
            ) {
                RepositoryIssuesScreen(
                    it.arguments?.getString("user"),
                    it.arguments?.getString("repo"),
                ) {
                    navController.popBackStack()
                }

            }
        }
    }
}