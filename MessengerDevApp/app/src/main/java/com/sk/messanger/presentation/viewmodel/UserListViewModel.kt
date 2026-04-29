package com.sk.messanger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sk.messanger.data.model.User
import com.sk.messanger.domain.usecase.UserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: UserListUseCase
) : ViewModel() {

    private val _usersState = MutableLiveData<UsersState>()
    val usersState: LiveData<UsersState> = _usersState
    fun fetchUsers(currentUserId: String) {
        _usersState.value = UsersState.Loading
        getUsersUseCase.invokeFetchUserList(currentUserId) { result ->
            result.fold(
                onSuccess = { users ->
                    if (users.isEmpty()) {
                        _usersState.value = UsersState.Empty
                    } else {
                        _usersState.value = UsersState.Success(users)
                    }
                },
                onFailure = { e ->
                    _usersState.value = UsersState.Error(e.message ?: "Failed to load users")
                }
            )
        }
    }
}
sealed class UsersState {
    object Loading : UsersState()
    object Empty   : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String)     : UsersState()
}