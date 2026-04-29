package com.sk.messanger.data.model

/**
 * @author Sudhakar
 * @date 09-04-2026
 */
data class User(val mobileNo: String? = "",
                val name: String? = "",
                val email: String? = "",
                val password: String? = "",
                var uid: String? = "")

data class UserContact(val mobileNo: String? = "",
                val name: String? = "",
                val email: String? = "",
                val password: String? = "",
                val uid: String? = "")
