package com.evgenii.githubviewer

import android.app.Application
import com.evgenii.githubviewer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubViewerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GitHubViewerApp)
            modules(appModule)
        }
    }
}