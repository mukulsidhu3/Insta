package com.example.insta.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage

class InstaApplication : Application() {

    lateinit var fAuth: FirebaseAuth
    lateinit var fDatabase: FirebaseDatabase
    lateinit var fStorage: FirebaseStorage

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Firebase.initialize(this)
        fAuth = Firebase.auth
        fDatabase = FirebaseDatabase.getInstance()
        fStorage = FirebaseStorage.getInstance()
    }
}