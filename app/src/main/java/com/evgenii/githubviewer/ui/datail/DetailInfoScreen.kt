package com.evgenii.githubviewer.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailInfoScreen(
    viewModel: RepositoryInfoViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (val currentState = state) {

        is RepositoryInfoViewModel.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is RepositoryInfoViewModel.State.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is RepositoryInfoViewModel.State.Loaded -> {
            val repo = currentState.githubRepo

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onBack) {
                        Text("<- Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = repo.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (repo.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = repo.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.webUrl))
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = " \uD83D\uDD17  github.com/${repo.ownerLogin}/${repo.name}",
                        fontSize = 16.sp
                        )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (repo.license != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "License",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(text = repo.license)
                    }
                }

                if (repo.language != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Language",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(text = repo.language)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("⭐  ${repo.starsCount}")
                    Text("Ψ ${repo.forksCount}")
                    Text("\uD83D\uDC41  ${repo.watchersCount}")
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                when (val readme = currentState.readmeState) {
                    is RepositoryInfoViewModel.ReadmeState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    is RepositoryInfoViewModel.ReadmeState.Empty -> {
                    }

                    is RepositoryInfoViewModel.ReadmeState.Error -> {
                        Text(
                            text = readme.error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is RepositoryInfoViewModel.ReadmeState.Loaded -> {
                        Text(
                            text = readme.markdown,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}