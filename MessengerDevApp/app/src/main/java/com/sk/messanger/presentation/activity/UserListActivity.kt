package com.sk.messanger.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.sk.messanger.R
import com.sk.messanger.data.model.User
import com.sk.messanger.databinding.ActivityUserListBinding
import com.sk.messanger.presentation.activity.LoginActivity.Companion.currentUserID
import com.sk.messanger.presentation.adapter.UserContactListAdapter
import com.sk.messanger.presentation.viewmodel.UserListViewModel
import com.sk.messanger.presentation.viewmodel.UsersState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: UserContactListAdapter
    private val contactList : ArrayList<User> = arrayListOf<User>()
    private val viewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
        /*val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerContacts.layoutManager = layoutManager

        fetchContacts()*/
        setupRecyclerView()
        observeUsersState()

        // Fetch users excluding the logged-in user
        viewModel.fetchUsers(currentUserID)

    }

    private fun setupRecyclerView() {
        binding.recyclerContacts.layoutManager = LinearLayoutManager(this)
    }

    private fun observeUsersState() {
        viewModel.usersState.observe(this) { state ->
            when (state) {
                is UsersState.Loading -> setLoading(true)
                is UsersState.Empty   -> {
                    setLoading(false)
                    Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
                }
                is UsersState.Success -> {
                    setLoading(false)
                    showUsers(state.users)
                }
                is UsersState.Error   -> {
                    setLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showUsers(users: List<User>) {
        val adapter = UserContactListAdapter(users) { contactUser ->
            openChat(contactUser)
        }
        binding.recyclerContacts.adapter = adapter
    }

    private fun setLoading(isLoading: Boolean) {
        // Optional: show/hide a progress bar if you add one to the layout
        // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun openChat(contactUser : User) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("receiverId", contactUser.uid)
        intent.putExtra("receiverName", contactUser.name)
        startActivity(intent)
    }

    private fun fetchContacts() {
        FirebaseDatabase.getInstance()
            .getReference("ChatUsers")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                for(snap in snapshot.children) {
                    val user = snap.getValue(User::class.java)
                    if (user?.uid != currentUserID) {
                        contactList.add(user!!)
                    }
                }
                adapter = UserContactListAdapter(contactList) { contactUser ->
                    openChat(contactUser)
                }
                binding.recyclerContacts.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}