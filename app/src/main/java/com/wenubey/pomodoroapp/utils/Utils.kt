package com.wenubey.pomodoroapp.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext

fun makeToast(message: String, context: Context){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}