package com.sk.messanger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sk.messanger.data.model.ChatMessage
import com.sk.messanger.domain.usecase.ListenMessagesUseCase
import com.sk.messanger.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val listenMessagesUseCase: ListenMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val _messagesState = MutableLiveData<ChatState>()
    val messagesState: LiveData<ChatState> = _messagesState

    private val _sendState = MutableLiveData<SendState>()
    val sendState: LiveData<SendState> = _sendState
    fun startListening(roomId: String) {
        _messagesState.value = ChatState.Loading
        listenMessagesUseCase.invokeListenMessages(
            roomId    = roomId,
            onSuccess = { messages ->
                _messagesState.value = ChatState.Success(messages)
            },
            onError   = { error ->
                _messagesState.value = ChatState.Error(error)
            }
        )
    }

    fun sendMessage(roomId: String, text: String, senderId: String) {
        sendMessageUseCase.invoke(
            roomId    = roomId,
            text      = text,
            senderId  = senderId,
            onSuccess = { _sendState.value = SendState.Sent },
            onError   = { error -> _sendState.value = SendState.Error(error) }
        )
    }
}

sealed class ChatState {
    object Loading : ChatState()
    data class Success(val messages: List<ChatMessage>) : ChatState()
    data class Error(val message: String) : ChatState()
}
sealed class SendState {
    object Sent : SendState()
    data class Error(val message: String) : SendState()
}