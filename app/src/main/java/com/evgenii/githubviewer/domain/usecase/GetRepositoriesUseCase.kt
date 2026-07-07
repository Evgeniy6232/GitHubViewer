package com.evgenii.githubviewer.domain.usecase

import com.evgenii.githubviewer.domain.model.Repo
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository

class GetRepositoriesUseCase(private val repository: GitHubRepoRepository) {

    suspend operator fun invoke(): Result<List<Repo>> {
        return try {
            val repos = repository.getRepositories()
            if (repos.isEmpty()) {
                Result.success(emptyList())
            } else {
                Result.success(repos)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}