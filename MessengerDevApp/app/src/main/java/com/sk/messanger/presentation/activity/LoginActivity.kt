package com.sk.messanger.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sk.messanger.R
import com.sk.messanger.data.model.User
import com.sk.messanger.databinding.ActivityLoginBinding
import com.sk.messanger.presentation.viewmodel.AuthState
import com.sk.messanger.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: FirebaseDatabase
    private var ref: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
    companion object {
        var currentUserID : String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLoginMode()
        setupClickListeners()
        observeLoginSignup()

    }
    private fun setupLoginMode() {
        binding.tvLogin.visibility = View.GONE
        binding.btnSignup.visibility = View.GONE
        binding.tvSignup.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.VISIBLE
        binding.etName.visibility = View.GONE
        binding.etMobile.visibility = View.GONE
    }
    private fun setupSignupMode() {
        binding.tvLogin.visibility   = View.VISIBLE
        binding.btnSignup.visibility  = View.VISIBLE
        binding.tvSignup.visibility   = View.GONE
        binding.btnLogin.visibility   = View.GONE
        binding.etName.visibility    = View.VISIBLE
        binding.etMobile.visibility   = View.VISIBLE
        clearFields()
    }
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }
    private fun clearFields() {
        binding.etEmail.setText("")
        binding.etMobile.setText("")
        binding.etName.setText("")
        binding.etPassword.setText("")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_signup -> {
                setupSignupMode()
            }
            R.id.btn_signup -> {
                /*binding.btnLogin.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
                val email = binding.etEmail.text.toString()
                val mobile = binding.etMobile.text.toString()
                val name = binding.etName.text.toString()
                val password = binding.etPassword.text.toString()
                val user = User(mobile, name, email, password)*/
                signUpMethod()
            }
            R.id.btn_login -> {
                loginMethod()
            }
            R.id.tv_login -> {
                setupLoginMode();
                clearFields()
            }
        }
    }

    private fun loginMethod() {
        val email    = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        viewModel.login(email,password)
    }
    private fun signUpMethod() {
        val email = binding.etEmail.text.toString()
        val mobile = binding.etMobile.text.toString()
        val name = binding.etName.text.toString()
        val password = binding.etPassword.text.toString()
        //val user = User(mobile, name, email, password)
        viewModel.signup(mobile,name,email,password)
    }

    private fun navigateToUserList() {
        startActivity(Intent(this@LoginActivity, UserListActivity::class.java))
        finish() // prevent going back to login screen
    }
    private fun observeLoginSignup() {
        viewModel.authState.observe(this) { state ->
            when(state) {
                is AuthState.Idle -> {
                    setLoading(false)
                }
                is AuthState.Loading -> setLoading(true)
                is AuthState.Success -> {
                    setLoading(false)
                    currentUserID = state.uid
                    navigateToUserList()
                }
                is AuthState.Error -> {
                    setLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
            }
        }
    }
    private fun setLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled  = !isLoading
        binding.btnSignup.isEnabled = !isLoading
    }

/*
    private fun signupMethod(user: User) {
        */
/*auth.signInWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener {
                val senderID = auth.uid!!

            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }*//*

        auth.createUserWithEmailAndPassword(user.email!!,user.password!!)
            .addOnSuccessListener { result ->
                val uid : String = result.user!!.uid// Firebase auth client uid
                user.uid = uid
                FirebaseDatabase.getInstance()
                    .getReference("ChatUsers")// Firebase database reference user list
                    .child(uid)// Firebase database uid
                    .setValue(user)
                currentUserID = uid
                //startActivity(Intent(this, UserListActivity::class.java))
                navigateToUserList()
            }
            .addOnFailureListener {
                Log.e("AUTH", "Error: ${it.message}")
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
*/


}