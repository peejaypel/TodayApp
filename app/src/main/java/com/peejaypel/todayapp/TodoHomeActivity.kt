package com.peejaypel.todayapp

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalTime


class TodoHomeActivity() : AppCompatActivity() {
    lateinit var todos: ArrayList<Todo>

    private lateinit var username: String
    private val INTERVAL = 60000//ms * s

    var mHandler: Handler = Handler()

    var checkForMessagesToBeSent: Runnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            for (todo in todos) {
                if (LocalDate.parse(todo.dateTarget) == LocalDate.now()) {
                    val tdTime = LocalTime.parse(todo.timeTarget)
                    println("Todo ${todo.id} is due at $tdTime")
                    println("Today is ${LocalTime.now()} and will be ${LocalTime.now().plusMinutes(30)} in 30 minutes")
                    if (LocalTime.now().plusMinutes(30).hour == tdTime.hour && LocalTime.now().plusMinutes(30).minute == tdTime.minute){
                        println("Todo ${todo.id} is due in 30 minutes, at $tdTime")
                        sendMessage(todo)
                    }

                }
            }
            mHandler.postDelayed(this, INTERVAL.toLong())
        }
    }

    fun startRepeatingTask() {
        checkForMessagesToBeSent.run()
    }

    fun stopRepeatingTask() {
        mHandler.removeCallbacks(checkForMessagesToBeSent)
    }


    private fun sendMessage(todo: Todo) {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val phoneNumber = DBHelper(this).getPhoneNumberByUserName(username)
        print("sendMessage(): Got local phone number $phoneNumber")
        println("sendMessage(): Attempting to send message...")

        val message = "==TodayApp #${todo.id}==" +
                "\nTitle: ${todo.title}" +
                "\nDescription: ${todo.description}" +
                "\nDue Date: ${todo.dateTarget}" +
                "\nDue Time: ${todo.timeTarget}"
        val smsManager: SmsManager = SmsManager.getDefault()
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null)
        Toast.makeText(this, " Message Sent", Toast.LENGTH_SHORT).show()
        println("sendMessage(): Message sent!")
        todo.isMessageOn = 0
        DBHelper(this).updateTodo(todo)
        refreshTodoList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        if (bundle != null) {
            username = bundle.getString("username").toString()
        }

        setContentView(R.layout.activity_todo_home)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        tvGreeting.text = "Hi, $username!"

        val tvPhoneNumber = findViewById<TextView>(R.id.tvPhoneNumber)
        tvPhoneNumber.text = DBHelper(this).getPhoneNumberByUserName(username)

        refreshTodoList()
//        checkRequiredPermissions()
        stopRepeatingTask()
        startRepeatingTask()

        val btnAddTodo = findViewById<FloatingActionButton>(R.id.btnAddTodo)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)

        btnAddTodo.setOnClickListener {
            val intent = Intent(this, TodoAddActivity::class.java)
            intent.putExtra("userId", DBHelper(this).getUserIdFromUsername(username))
            startActivity(intent)
            //DBHelper(this).addTodo("1", "Test1", "Description 1", "July 20, 2001")
            //println("Added")
            refreshTodoList()
        }

        btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
    }

    fun refreshTodoList() {
        val rvTodos = findViewById<RecyclerView>(R.id.rvTodo)
        todos = DBHelper(this).getTodosByUserName(username)
        val adapter = TodoAdapter(todos)

        println("${adapter.itemCount} =======================================================")
        if (adapter.itemCount > 0) {
            rvTodos.adapter = adapter
            rvTodos.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTodoList()
        println("refreshing on resume")
    }


}