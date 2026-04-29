package com.sk.messanger.domain.usecase

import com.sk.messanger.data.model.ChatMessage
import com.sk.messanger.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    fun invoke(
        roomId: String,
        text: String,
        senderId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (text.isBlank()) {
            onError("Message cannot be empty")
            return
        }
        val message = ChatMessage(
            senderId = senderId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        chatRepository.sendMessage(roomId, message, onSuccess, onError)
    }
}