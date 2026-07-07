package com.evgenii.githubviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDto (
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    @SerialName("stargazers_count")
    val starsCount: Int,
    @SerialName("forks_count")
    val forksCount: Int
)

/** JSON from GitHub (GET/user/repos)
 *   {
 *     "id": 1234,
 *     "name": "project",
 *     "description": "Dscription project",
 *     "language": "Kotlin",
 *     "stargazers_count": 3,
 *     "forks_count": 2
 *   }
 */
