package com.sk.messanger.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.logger.Logger
import com.sk.messanger.R
import com.sk.messanger.data.model.ChatMessage
import com.sk.messanger.databinding.ActivityChatBinding
import com.sk.messanger.presentation.activity.LoginActivity.Companion.currentUserID
import com.sk.messanger.presentation.adapter.ChatAdapter
import com.sk.messanger.presentation.viewmodel.ChatState
import com.sk.messanger.presentation.viewmodel.ChatViewModel
import com.sk.messanger.presentation.viewmodel.SendState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(),View.OnClickListener {
    lateinit var binding : ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var adapter: ChatAdapter
    private val messageList = ArrayList<ChatMessage>()

    private lateinit var receiverId: String
    private lateinit var receiverName: String
    private var roomId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        getIntentData()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        observeMessages()
        observeSendState()

        viewModel.startListening(roomId)



    }

    // ─── Setup ────────────────────────────────────────────────────────────────

    private fun getIntentData() {
        receiverId   = intent.getStringExtra("receiverId") ?: ""
        receiverName = intent.getStringExtra("receiverName") ?: ""
        roomId = buildRoomId(currentUserID, receiverId)
    }

    private fun setupToolbar() {
        binding.tvReceiverName.text = receiverName
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(currentUserID, messageList)
        binding.recyclerChat.adapter      = adapter
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener(this)
        binding.imgBtnBack.setOnClickListener(this)
    }

    // ─── Click Handling ───────────────────────────────────────────────────────

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.img_btn_back -> {
                startActivity(Intent(this, UserListActivity::class.java))
                finish()
            }
            R.id.btnSend -> {
                val text = binding.etMessage.text.toString().trim()
                if (text.isNotEmpty()) {
                    viewModel.sendMessage(roomId, text, currentUserID)
                    binding.etMessage.setText("")
                }
            }
        }
    }

    // ─── Observe LiveData ─────────────────────────────────────────────────────

    private fun observeMessages() {
        viewModel.messagesState.observe(this) { state ->
            when (state) {
                is ChatState.Loading -> { /* optional: show shimmer or progress */ }
                is ChatState.Success -> showMessages(state.messages)
                is ChatState.Error   -> Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeSendState() {
        viewModel.sendState.observe(this) { state ->
            when (state) {
                is SendState.Sent  -> { /* message sent — list will auto-update via listener */ }
                is SendState.Error -> Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun showMessages(messages: List<ChatMessage>) {
        messageList.clear()
        messageList.addAll(messages)
        adapter.notifyDataSetChanged()
        if (messageList.isNotEmpty()) {
            binding.recyclerChat.scrollToPosition(messageList.size - 1)
        }
    }

    // Builds a consistent room ID regardless of who opens the chat first
    private fun buildRoomId(currentUserId: String, receiverId: String): String {
        return if (currentUserId > receiverId) {
            "${currentUserId}_$receiverId"
        } else {
            "${receiverId}_$currentUserId"
        }
    }
}