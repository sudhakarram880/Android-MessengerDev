package com.sk.messanger.domain.usecase

import com.sk.messanger.data.model.User
import com.sk.messanger.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 21-04-2026
 */
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun invokeSignUp(user: User, onResult: (Result<String>) -> Unit) {
        if (user.email.isNullOrBlank() || user.password.isNullOrBlank()) {
            onResult(Result.failure(Exception("Email and password must not be empty")))
            return
        }
        if (user.name.isNullOrBlank()) {
            onResult(Result.failure(Exception("Name must not be empty")))
            return
        }
        authRepository.signUp(user, onResult)
    }
}