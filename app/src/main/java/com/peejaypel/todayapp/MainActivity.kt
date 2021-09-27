package com.peejaypel.todayapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val inputLoginUsername = findViewById<EditText>(R.id.login_username)
        val inputLoginPassword = findViewById<EditText>(R.id.login_password)

        val db = DBHelper(this)

        btnLogin.setOnClickListener{
            if (db.isRegistered(inputLoginUsername.text.toString())){
                if (db.isValidCredentials(inputLoginUsername.text.toString(), inputLoginPassword.text.toString())){
                    val intent = Intent(this, TodoHomeActivity::class.java)
                    intent.putExtra("username", inputLoginUsername.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}