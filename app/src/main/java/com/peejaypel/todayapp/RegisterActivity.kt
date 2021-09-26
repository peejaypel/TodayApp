package com.peejaypel.todayapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnRead = findViewById<Button>(R.id.btnRead)
        val btnClearUsers = findViewById<Button>(R.id.btnClear)

        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        var inputUsername = findViewById<EditText>(R.id.register_username)
        var inputPassword = findViewById<EditText>(R.id.register_password)
        var inputPasswordConfirm = findViewById<EditText>(R.id.register_password_confirmation)
        val db = DBHelper(this)

        btnRegister.setOnClickListener{
            println("$inputPassword =?= $inputPasswordConfirm")
            if (inputPassword.text.toString() == inputPasswordConfirm.text.toString()) {
                if (!db.isRegistered(inputUsername.text.toString())) {
                    db.register(inputUsername.text.toString(), inputPassword.text.toString())
                } else {
                    Toast.makeText(this, "User is already registered", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener{
            val data = db.getUserTable()
            tvRegister.text = ""

            for (i in 0 until data.size) {
                tvRegister.append(
                    "${data[i].userID} : ${data[i].username} : ${data[i].password}\n"
                )
            }
        }

        btnClearUsers.setOnClickListener{
            db.dropUsersTable()
        }
    }


}