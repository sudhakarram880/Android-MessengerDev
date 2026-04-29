package com.sk.messanger.domain.usecase

import com.sk.messanger.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 21-04-2026
 */
class LoginUseCase @Inject constructor(val authRepository: AuthRepository) {
    fun loginInvoke(email: String, password: String, onResult: (Result<String>) -> Unit) {
        if (email.isNullOrEmpty()) {
            onResult(Result.failure(Exception("Please provide email")))
        } else if (password.isNullOrEmpty()) {
            onResult(Result.failure(Exception("Please provide password")))
        } else {
            //call the login
            authRepository.login(email,password,onResult)
        }
    }
}