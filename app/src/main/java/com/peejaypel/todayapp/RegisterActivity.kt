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

        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        var inputUsername = findViewById<EditText>(R.id.register_username)
        var inputPassword = findViewById<EditText>(R.id.register_password)
        var inputPasswordConfirm = findViewById<EditText>(R.id.register_password_confirmation)
        var inputPhoneNumber = findViewById<EditText>(R.id.register_phone_number)
        val db = DBHelper(this)

        btnRegister.setOnClickListener {
            println("$inputPassword =?= $inputPasswordConfirm")
            if (!db.isRegistered(inputUsername.text.toString())) {
                if (inputPassword.text.toString() == inputPasswordConfirm.text.toString()) {
                    db.register(inputUsername.text.toString(), inputPassword.text.toString(), inputPhoneNumber.text.toString())
                    Toast.makeText(this,"Succesfully registered!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User is already registered", Toast.LENGTH_SHORT).show()
            }
        }
    }


}