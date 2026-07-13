package com.evgenii.githubviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.evgenii.githubviewer.data.local.TokenStorage
import com.evgenii.githubviewer.ui.navigation.NavGraph
import com.evgenii.githubviewer.ui.navigation.Routes
import com.evgenii.githubviewer.ui.theme.GitHubViewerTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val tokenStorage: TokenStorage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = if (tokenStorage.authToken != null) {
            Routes.LIST
        } else {
            Routes.AUTH
        }

        setContent {
            GitHubViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
