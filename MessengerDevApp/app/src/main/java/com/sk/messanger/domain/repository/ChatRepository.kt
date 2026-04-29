package com.sk.messanger.domain.repository

import com.sk.messanger.data.model.ChatMessage

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
interface ChatRepository {
    fun sendMessage(roomId: String, message: ChatMessage, onSuccess : () -> Unit, onFailure : (String) -> Unit)
    fun startListenMessages(
        roomId: String,
        onSuccess: (List<ChatMessage>) -> Unit,
        onError: (String) -> Unit
    )
}