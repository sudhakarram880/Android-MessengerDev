package com.sk.messanger.data.model

/**
 * @author Sudhakar
 * @date 10-04-2026
 */
data class ChatMessage(
    val messageId: String? = "",
    val senderId: String? = "",
    val text: String? = "",
    val timestamp: Long? = 0L
)
