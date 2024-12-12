package com.example.ubtrace.domain.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubtrace.data.auth.AuthRepository
import com.example.ubtrace.data.auth.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository: AuthRepository = AuthRepositoryImpl() // Membuat AuthRepository secara manual
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.login(email, password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success(result.getOrNull())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun register(email: String, password: String, username: String, noTelp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.register(email, password, username, noTelp)
            _authState.value = if (result.isSuccess) {
                AuthState.Success(result.getOrNull())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: String?) : AuthState()
    data class Error(val errorMessage: String) : AuthState()
}
