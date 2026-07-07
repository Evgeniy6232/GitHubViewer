package com.evgenii.githubviewer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val login: String,
    val name: String?,
    @SerialName("avatar_url")
    val avatarUrl: String
)