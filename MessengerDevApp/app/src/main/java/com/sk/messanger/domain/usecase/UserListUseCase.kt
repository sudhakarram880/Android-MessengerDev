package com.sk.messanger.domain.usecase

import com.sk.messanger.data.model.User
import com.sk.messanger.domain.repository.UserRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
class UserListUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun invokeFetchUserList(currentUserId : String, onUserResult : (Result<List<User>>) -> Unit) {
        userRepository.fetchUserList { result ->
            result.fold(
                onSuccess = { users ->
                    // Filter out the currently logged-in user
                    val filtered = users.filter { it.uid != currentUserId } // Except current user
                    onUserResult(Result.success(filtered))

                },
                onFailure = { e ->
                    onUserResult(Result.failure(e))
                }

            )
        }
    }
}