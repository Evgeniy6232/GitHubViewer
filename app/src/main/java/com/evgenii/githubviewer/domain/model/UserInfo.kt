package com.evgenii.githubviewer.domain.model

/**
 * avatarUrl пока не будет использоваться,
 * но в будущем хочется иметь приветственный экран,
 * чтобы пользователь наглядно видел в какой акк вошел
 */

data class UserInfo(
    val login: String,
    val name: String?,
    val avatarUrl: String
)