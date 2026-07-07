package com.evgenii.githubviewer.domain.usecase

import com.evgenii.githubviewer.domain.model.UserInfo
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository

class SignInUseCase(private val repository: GitHubRepoRepository) {

    suspend operator fun invoke(token: String): Result<UserInfo> {
        if (token.isBlank()) {
            return Result.failure(IllegalArgumentException("Token can't be empty"))
        }
        return repository.getUserInfo(token)
    }
}
