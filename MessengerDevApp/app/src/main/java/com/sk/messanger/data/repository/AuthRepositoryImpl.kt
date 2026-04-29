package com.sk.messanger.data.repository

import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sk.messanger.data.model.User
import com.sk.messanger.domain.repository.AuthRepository
import com.sk.messanger.presentation.activity.LoginActivity.Companion.currentUserID
import com.sk.messanger.presentation.activity.MainActivity
import javax.inject.Inject

/**
 * @author Sudhakar
 * @date 21-04-2026
 */

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase) : AuthRepository {
    override fun login(
        email: String,
        password: String,
        onResult: (Result<String>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                System.out.printf("UID =====> " + uid)
                if (uid != null) {
                    onResult(Result.success(uid))
                } else {
                    onResult(Result.failure(Exception("Failed to login")))
                }

            }
            .addOnFailureListener {
                onResult(Result.failure(exception = Exception(it.message)))//Failure message
            }
    }

    override fun signUp(
        user: User,
        onResult: (Result<String>) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(user.email!!,user.password!!)
            .addOnSuccessListener { result ->//Creation Success Listener
                val uid = result.user?.uid
                if (uid != null) {
                    val newUser = user.copy(uid = uid)
                    firebaseDatabase.getReference("ChatUsers")//Create User Login(Table or Path) Data in Database
                        .child(uid)
                        .setValue(newUser)
                        .addOnSuccessListener {
                            onResult(Result.success(uid))
                        }
                        .addOnFailureListener { exception ->
                            onResult(Result.failure(Exception(exception)))
                        }

                } else {
                    onResult(Result.failure(Exception("UID not found")))
                }
            }.addOnFailureListener { exception ->//Creation Failure
                onResult(Result.failure(exception))
            }
    }

}