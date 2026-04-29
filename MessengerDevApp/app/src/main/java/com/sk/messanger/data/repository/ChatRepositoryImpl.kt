package com.sk.messanger.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sk.messanger.data.model.ChatMessage
import com.sk.messanger.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
class ChatRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ChatRepository {

    override fun startListenMessages(
        roomId: String,
        onSuccess: (List<ChatMessage>) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseDatabase
            .getReference("chats")
            .child(roomId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<ChatMessage>()
                    for (snap in snapshot.children) {
                        val message = snap.getValue(ChatMessage::class.java)
                        if (message != null) messages.add(message)
                    }
                    onSuccess(messages)
                }

                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    override fun sendMessage(
        roomId: String,
        message: ChatMessage,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val ref = firebaseDatabase
            .getReference("chats")
            .child(roomId)
            .push()



        val messageWithId = message.copy(messageId = ref.key ?: "")

        ref.setValue(messageWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to send message") }
    }
}