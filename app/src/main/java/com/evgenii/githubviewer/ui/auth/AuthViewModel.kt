package com.evgenii.githubviewer.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.githubviewer.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    sealed interface State {
        data object AwaitingInput: State
        data object Loading: State
        data class InvalidInput(val message: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        data object RouteToMain : Action
    }

    val token = MutableStateFlow("")
    private val _state = MutableStateFlow<State>(State.AwaitingInput)
    val state: StateFlow<State> = _state

    private val _actions = MutableSharedFlow<Action>()
    val actions: SharedFlow<Action> = _actions

    fun onSignButtonPressed() {
        val currentToken = token.value.trim()

        if (currentToken.isBlank()) {
            _state.value = State.InvalidInput("Token can't be empty")
            return
        }

        viewModelScope.launch {
            _state.value = State.Loading

            signInUseCase(currentToken)
                .onSuccess {
                    _state.value = State.AwaitingInput
                    _actions.emit(Action.RouteToMain)
                }
                .onFailure { error ->
                    _state.value = State.InvalidInput("Invalid token")
                }
        }
    }
}