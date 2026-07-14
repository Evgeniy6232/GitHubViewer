package com.evgenii.githubviewer.data.repository

import com.evgenii.githubviewer.data.local.TokenStorage
import com.evgenii.githubviewer.data.remote.api.GitHubApi
import com.evgenii.githubviewer.data.remote.dto.RepoDetailsDto
import com.evgenii.githubviewer.data.remote.dto.RepoDto
import com.evgenii.githubviewer.data.remote.dto.UserDto
import com.evgenii.githubviewer.domain.model.Repo
import com.evgenii.githubviewer.domain.model.RepoDetails
import com.evgenii.githubviewer.domain.model.UserInfo
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository

class GitHubRepoRepositoryImpl (
    private val api: GitHubApi,
    private val tokenStorage: TokenStorage
) : GitHubRepoRepository {

    private var ownerLogin: String? = null

    override suspend fun getUserInfo(token: String): Result<UserInfo> {
        return try {
            val userDto = api.getUserInfo(token)
            tokenStorage.authToken = token
            ownerLogin = userDto.login
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRepositories(): List<Repo> {
        val dtos = api.getRepositories()
        return dtos.map { it.toDomain() }
    }

    override suspend fun getRepository(repoId: String): RepoDetails {
        val owner = ownerLogin ?: throw IllegalStateException("Failed to login to account")
        val dto = api.getRepository(owner, repoId)
        return dto.toDomain()
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String {
        return try {
            api.getRepositoryReadme(ownerName, repositoryName, branchName)
        } catch (e: Exception) {
            ""
        }
    }

    private fun UserDto.toDomain() = UserInfo(
        login = login,
        name = name,
        avatarUrl = avatarUrl
    )

    private fun RepoDto.toDomain() = Repo(
        id = id.toString(),
        name = name,
        description = description ?: "",
        language = language,
        starsCount = starsCount,
        forksCount = forksCount
    )

    private fun RepoDetailsDto.toDomain() = RepoDetails(
        id = id.toString(),
        name = name,
        description = description ?: "",
        language = language,
        starsCount = starsCount,
        forksCount = forksCount,
        watchersCount = watchersCount,
        license = license?.name,
        webUrl = webUrl,
        ownerLogin = ownerLogin!!,
        defaultBranch = defaultBranch
    )
}