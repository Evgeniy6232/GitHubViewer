package com.evgenii.githubviewer.domain.model

data class Repo(
    val id: String,
    val name: String,
    val description: String,
    val language: String?,
    val starsCount: Int,
    val forksCount: Int
)