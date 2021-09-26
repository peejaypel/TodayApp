package com.peejaypel.todayapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TodoHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_home)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}