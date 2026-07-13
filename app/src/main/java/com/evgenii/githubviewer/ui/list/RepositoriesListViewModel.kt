package com.evgenii.githubviewer.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.githubviewer.domain.model.Repo
import com.evgenii.githubviewer.domain.usecase.GetRepositoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepositoriesListViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModel() {

    sealed interface State {
        data object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        data object Empty : State
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    init {
        loadRepositories()
    }

    fun loadRepositories() {
        viewModelScope.launch {
            _state.value = State.Loading

            getRepositoriesUseCase().onSuccess { repos ->
                _state.value = if (repos.isEmpty()) {
                    State.Empty
                } else {
                    State.Loaded(repos)
                }
            }.onFailure { error ->
                _state.value = State.Error(error.message ?: "Loading error")
            }
        }
    }

}