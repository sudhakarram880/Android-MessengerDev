package com.sk.messanger.domain.repository

import com.sk.messanger.data.model.User

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
interface UserRepository {
    fun fetchUserList(onUserResult : (Result<List<User>>) -> Unit )
}