package com.evgenii.githubviewer.domain.usecase

import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository

class GetRepositoryReadmeUseCase(
    private val repository: GitHubRepoRepository
) {
    suspend operator fun invoke(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): Result<String> {
        return try {
            val readme = repository.getRepositoryReadme(ownerName, repositoryName, branchName)
            Result.success(readme)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}