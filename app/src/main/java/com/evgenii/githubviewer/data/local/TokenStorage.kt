package com.evgenii.githubviewer.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var authToken: String?
        get() = prefs.getString("github_token", null)
        set(value) {
            prefs.edit { putString("github_token", value) }
        }
}