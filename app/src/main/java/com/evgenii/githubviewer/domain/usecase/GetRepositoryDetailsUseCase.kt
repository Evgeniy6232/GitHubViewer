package com.evgenii.githubviewer.domain.usecase

import com.evgenii.githubviewer.domain.model.RepoDetails
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository

class GetRepositoryDetailsUseCase(private val repository: GitHubRepoRepository) {

    suspend operator fun invoke(repoId: String): Result<RepoDetails> {
        if (repoId.isBlank()) {
            return Result.failure(IllegalArgumentException("ID repositoru can't be empty"))
        }
        return try {
            val details = repository.getRepository(repoId)
            Result.success(details)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}