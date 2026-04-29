package com.sk.messanger.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sk.messanger.R
import com.sk.messanger.data.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author Sudhakar
 * @date 10-04-2026
 */
class ChatAdapter(
    private val userId: String,
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SENT = 1
    private val TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == userId) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_sent, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_received, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]

        if (holder is SentViewHolder) {
            holder.tv.text = msg.text
            if (msg.timestamp != null) {
                var date = Date(msg.timestamp!!)
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                //holder.tv.text = sdf.format(date)

            }
        } else if (holder is ReceivedViewHolder) {
            holder.tv.text = msg.text
            if (msg.timestamp != null) {
                var date = Date(msg.timestamp!!)
                val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                //holder.tv.text = sdf.format(date)

            }
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tvMessageSent)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tvMessageReceived)
    }
}