package com.evgenii.githubviewer.domain.repository

import com.evgenii.githubviewer.domain.model.Repo
import com.evgenii.githubviewer.domain.model.RepoDetails
import com.evgenii.githubviewer.domain.model.UserInfo

interface GitHubRepoRepository {
    suspend fun getUserInfo(token: String): Result<UserInfo>
    suspend fun getRepositories(): List<Repo>
    suspend fun getRepository(repoId: String): RepoDetails
    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String
}

/**
 * getRepositoryReadme
 * !!!!!!!!!!!!! public/private in global general search !!!!!!!!!!!!!
 */
