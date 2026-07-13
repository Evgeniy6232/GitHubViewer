package com.evgenii.githubviewer.di

import com.evgenii.githubviewer.data.local.TokenStorage
import com.evgenii.githubviewer.data.remote.api.GitHubApi
import com.evgenii.githubviewer.data.repository.GitHubRepoRepositoryImpl
import com.evgenii.githubviewer.domain.repository.GitHubRepoRepository
import com.evgenii.githubviewer.domain.usecase.GetRepositoriesUseCase
import com.evgenii.githubviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.evgenii.githubviewer.domain.usecase.GetRepositoryReadmeUseCase
import com.evgenii.githubviewer.domain.usecase.SignInUseCase
import com.evgenii.githubviewer.ui.auth.AuthViewModel
import com.evgenii.githubviewer.ui.detail.RepositoryInfoViewModel
import com.evgenii.githubviewer.ui.list.RepositoriesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {

    single { TokenStorage(androidContext()) }
    single { GitHubApi(get()) }
    single<GitHubRepoRepository> { GitHubRepoRepositoryImpl(get(), get()) }

    factory { SignInUseCase(get()) }
    factory { GetRepositoriesUseCase(get()) }
    factory { GetRepositoryDetailsUseCase(get()) }
    factory { GetRepositoryReadmeUseCase(get()) }

    viewModel { AuthViewModel(get()) }
    viewModel { RepositoriesListViewModel(get()) }
    viewModel { params -> RepositoryInfoViewModel(params.get(), get(), get()) }
}