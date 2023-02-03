package com.example.insta.util

import android.content.Context
import android.widget.Toast

class ShowMessage {
    companion object {
        fun showMessage(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}