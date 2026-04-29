package com.sk.messanger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sk.messanger.data.model.User
import com.sk.messanger.domain.usecase.LoginUseCase
import com.sk.messanger.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 21-04-2026
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email : String, password : String) {
        _authState.value = AuthState.Loading
        loginUseCase.loginInvoke(email,password) { result ->
            result.fold(
                onSuccess = { uid -> _authState.value = AuthState.Success(uid) },
                onFailure = { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login Failed")
                }
            )
        }
    }
    fun signup(mobile: String, name: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        val user = User(mobileNo = mobile, name = name, email = email, password = password)
        signupUseCase.invokeSignUp(
            user,
            onResult = { result ->
                result.fold(
                    onSuccess = { uid -> _authState.value = AuthState.Success(uid) },
                    onFailure = { e  -> _authState.value = AuthState.Error(e.message ?: "Failed to Signup") }
                )
            }
        )
    }
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid : String) : AuthState()
    data class Error(val message : String) : AuthState()
}