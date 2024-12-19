package com.d29.auraplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.d29.auraplus.model.UserProfile
import com.d29.auraplus.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> = _authResult

    private val _currentUserName = MutableStateFlow("")
    val currentUserName: StateFlow<String> = _currentUserName

    private val _currentUserEmail = MutableStateFlow("")
    val currentUserEmail: StateFlow<String> = _currentUserEmail

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authResult.value = false
            _currentUserName.value = "Email y contraseña no pueden estar vacíos."
            _currentUserEmail.value = ""
            return
        }
        viewModelScope.launch {
            try {
                val user = repository.getUser(email, password)
                if (user != null) {
                    _authResult.value = true
                    _currentUserName.value = user.name
                    _currentUserEmail.value = user.email
                } else {
                    _authResult.value = false
                    _currentUserName.value = "Email o contraseña inválidos."
                    _currentUserEmail.value = ""
                }
            } catch (e: Exception) {
                _authResult.value = false
                _currentUserName.value = "Error inesperado: ${e.message}"
                _currentUserEmail.value = ""
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authResult.value = false
            _currentUserName.value = "Todos los campos son obligatorios."
            return
        }
        viewModelScope.launch {
            try {
                val existingUser = repository.getUser(email, password)
                if (existingUser != null) {
                    _authResult.value = false
                    _currentUserName.value = "El correo ya está registrado."
                } else {
                    val user = UserProfile(email, name, password)
                    repository.insertUser(user)
                    _authResult.value = true
                    _currentUserName.value = name
                    _currentUserEmail.value = email
                }
            } catch (e: Exception) {
                _authResult.value = false
                _currentUserName.value = "Error al registrar: ${e.message}"
            }
        }
    }

    fun logout() {
        _authResult.value = null
        _currentUserName.value = ""
        _currentUserEmail.value = ""
    }

    companion object {
        fun Factory(repository: UserRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(repository) as T
                }
            }
        }
    }
}