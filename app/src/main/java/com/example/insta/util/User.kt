package com.example.insta.util

data class User(
    val email: String,
    val followers: Int,
    val followings: Int,
    val fullName: String,
    val imageUri: String,
    val posts: List<String>
)
