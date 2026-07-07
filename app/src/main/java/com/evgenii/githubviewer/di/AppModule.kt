package com.evgenii.githubviewer.di

import com.evgenii.githubviewer.data.local.TokenStorage
import com.evgenii.githubviewer.data.remote.api.GitHubApi
import com.evgenii.githubviewer.data.repository.GitHubRepoRepositoryImpl
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository
import com.evgenii.githubviewer.domain.usecase.GetRepositoriesUseCase
import com.evgenii.githubviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.evgenii.githubviewer.domain.usecase.SignInUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single { TokenStorage(androidContext()) }
    single { GitHubApi(get()) }
    single<GitHubRepoRepository> { GitHubRepoRepositoryImpl(get(), get()) }

    factory { SignInUseCase(get()) }
    factory { GetRepositoriesUseCase(get()) }
    factory { GetRepositoryDetailsUseCase(get()) }
}