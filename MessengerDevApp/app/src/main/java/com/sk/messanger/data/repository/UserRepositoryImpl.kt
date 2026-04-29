package com.sk.messanger.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sk.messanger.data.model.User
import com.sk.messanger.domain.repository.UserRepository
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 24-04-2026
 */

class UserRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : UserRepository {

    override fun fetchUserList(onUserResult: (Result<List<User>>) -> Unit) {
        firebaseDatabase
            .getReference("ChatUsers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = mutableListOf<User>()
                    for (snap in snapshot.children) {
                        val user = snap.getValue(User::class.java)
                        if (user != null) {
                            users.add(user)
                        }
                    }
                    onUserResult(Result.success(users))
                }

                override fun onCancelled(error: DatabaseError) {
                    onUserResult(Result.failure(Exception(error.message)))
                }
            })
    }

}