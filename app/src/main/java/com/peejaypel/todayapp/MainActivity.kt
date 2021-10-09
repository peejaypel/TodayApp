package com.peejaypel.todayapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.SEND_SMS
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val inputLoginUsername = findViewById<EditText>(R.id.login_username)
        val inputLoginPassword = findViewById<EditText>(R.id.login_password)

        val db = DBHelper(this)

        btnLogin.setOnClickListener {
            if (db.isRegistered(inputLoginUsername.text.toString())) {
                if (db.isValidCredentials(
                        inputLoginUsername.text.toString(),
                        inputLoginPassword.text.toString()
                    )
                ) {
                    if (!hasPermissions(this, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_PHONE_STATE)
                    ) {
                        println("Permissions not granted")
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
                    }  else {
                        println("Permissions granted, proceeding")
                        val intent = Intent(this, TodoHomeActivity::class.java)
                        intent.putExtra("username", inputLoginUsername.text.toString())
                        startActivity(intent)
                    }

//                    if (hasPermissionsDenied(this, PERMISSIONS.toString())) {
//                        Toast.makeText(this, "Permissions have been denied. You may have to manually allow them on the app settings to continue.", Toast.LENGTH_SHORT).show()
//                    }
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun hasPermissionsDenied(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
    }

    private fun getRequiredPermissions() {
        println("Attempting to request permissions...")
        requestPermission(
            arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS).toString(),
            PERMISSION_ALL
        )
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, id -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }


}