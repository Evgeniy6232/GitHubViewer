package com.evgenii.githubviewer.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val token by viewModel.token.collectAsStateWithLifecycle()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is AuthViewModel.Action.ShowError -> {
                    //45678
                }
                is AuthViewModel.Action.RouteToMain -> {
                    onNavigateToMain()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GitHub Viewer",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = token,
            onValueChange = { viewModel.token.value = it},
            label = { Text("Personal Access Token")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = state !is AuthViewModel.State.Loading
        )

        if (state is AuthViewModel.State.InvalidInput) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = (state as AuthViewModel.State.InvalidInput).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onSignButtonPressed() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is AuthViewModel.State.Loading
        ) {
            if (state is AuthViewModel.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Войти")
        }
    }
}
