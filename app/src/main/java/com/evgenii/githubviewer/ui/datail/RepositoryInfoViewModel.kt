package com.evgenii.githubviewer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.githubviewer.domain.model.RepoDetails
import com.evgenii.githubviewer.domain.usecase.GetRepositoryDetailsUseCase
import com.evgenii.githubviewer.domain.usecase.GetRepositoryReadmeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepositoryInfoViewModel(
    private val repoId: String,
    private val getRepositoryDetailsUseCase: GetRepositoryDetailsUseCase,
    private val getRepositoryReadmeUseCase: GetRepositoryReadmeUseCase
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Error(val error: String) : State
        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        data object Loading : ReadmeState
        data object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    init {
        loadRepositoryDetails()
    }

    private fun loadRepositoryDetails() {
        viewModelScope.launch {
            _state.value = State.Loading

            getRepositoryDetailsUseCase(repoId)
                .onSuccess { details ->
                    _state.value = State.Loaded(
                        githubRepo = details,
                        readmeState = ReadmeState.Loading
                    )
                    loadReadme(details)
                }
                .onFailure { error ->
                    _state.value = State.Error(
                        error.message ?: "Error loading"
                    )
                }
        }
    }

    private fun loadReadme(repo: RepoDetails) {
        viewModelScope.launch {

            val current = _state.value as? State.Loaded ?: return@launch
            _state.value = current.copy(readmeState = ReadmeState.Loading)

            val defaultBranch = repo.defaultBranch

            getRepositoryReadmeUseCase(
                ownerName = repo.ownerLogin,
                repositoryName = repo.name,
                branchName = defaultBranch
            )
                .onSuccess { markdown ->
                    val updated = (_state.value as? State.Loaded) ?: return@launch
                    _state.value = updated.copy(
                        readmeState = if (markdown.isBlank()) {
                            ReadmeState.Empty
                        } else {
                            ReadmeState.Loaded(markdown)
                        }
                    )
                }
                .onFailure { error ->
                    val updated = (_state.value as? State.Loaded) ?: return@launch
                    _state.value = updated.copy(
                        readmeState = ReadmeState.Empty
                    )
                }
        }
    }
}