package com.sk.messanger.domain.usecase

import com.sk.messanger.data.model.ChatMessage
import com.sk.messanger.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
class ListenMessagesUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    fun invokeListenMessages(roomId: String, onSuccess: (List<ChatMessage>) -> Unit, onError: (String) -> Unit) {
        if (roomId.isBlank()) {
            onError("Room ID is invalid")
            return
        }
        chatRepository.startListenMessages(roomId, onSuccess, onError)

    }
}