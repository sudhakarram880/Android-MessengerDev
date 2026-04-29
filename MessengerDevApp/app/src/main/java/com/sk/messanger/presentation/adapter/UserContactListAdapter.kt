package com.sk.messanger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sk.messanger.data.model.User
import com.sk.messanger.databinding.ItemContentUsersBinding

/**
 * @author Sudhakar
 * @date 24-04-2026
 */
class UserContactListAdapter(
    private val users: List<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserContactListAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContentUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContentUsersBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val user = users[position]
        holder.binding.userName.text    = user.name
        holder.binding.userContact.text = user.mobileNo
        holder.binding.relativeContact.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int = users.size
}