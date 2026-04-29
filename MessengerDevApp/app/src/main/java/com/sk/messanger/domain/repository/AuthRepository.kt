package com.sk.messanger.domain.repository

import com.sk.messanger.data.model.User

/**
 * @author Sudhakar
 * @date 21-04-2026
 */
interface AuthRepository {
    fun login(email: String, password: String, onResult : (Result<String>) -> Unit)
    fun signUp(user : User, onResult : (Result<String>) -> Unit)
}