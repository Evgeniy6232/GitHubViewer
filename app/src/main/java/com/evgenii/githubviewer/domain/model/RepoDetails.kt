package com.evgenii.githubviewer.domain.model

data class RepoDetails(
    val id: String,
    val name: String,
    val description: String,
    val language: String?,
    val starsCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val license: String?,
    val webUrl: String,
    val ownerLogin: String,
    val defaultBranch: String
)