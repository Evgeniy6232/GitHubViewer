package com.evgenii.githubviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsDto(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    @SerialName("stargazers_count")
    val starsCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    val license: LicenseDto?,
    @SerialName("html_url")
    val webUrl: String,
    @SerialName("default_branch")
    val defaultBranch: String

)

@Serializable
data class LicenseDto(val name: String)

/**
 * JSON from GitHub (GET /repos/{owner}/{repo})
 *
 *   {
 *     "id": 123456,
 *     "name": "project",
 *     "description": "Description qwert",
 *     "language": "Kotlin",
 *     "stargazers_count": 12,
 *     "forks_count": 3,
 *     "watchers_count": 113,
 *     "license": {
 *       "name": "License"
 *     },
 *     "html_url": "ссылка на репу"
 *   }
 *
 * Гит выдает лицензию объектом - нужен отдельный класс, чтбы извлекать строку
 */