package com.peejaypel.todayapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoHomeActivity() : AppCompatActivity() {
    lateinit var todos: ArrayList<Todo>

    private lateinit var username:String
    override fun onCreate(savedInstanceState: Bundle?) {

        val bundle = intent.extras
        if (bundle != null) {
            username = bundle.getString("username").toString()
        }

        setContentView(R.layout.activity_todo_home)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        tvGreeting.text = "Hi, $username!"

        refreshTodoList()

        val btnAddTodo = findViewById<FloatingActionButton>(R.id.btnAddTodo)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)

        btnAddTodo.setOnClickListener{
            val intent = Intent(this, TodoAddActivity::class.java)
            intent.putExtra("username", username)
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

    fun refreshTodoList(){
        val rvTodos = findViewById<RecyclerView>(R.id.rvTodo)
        todos = DBHelper(this).getTodosByUserId("1")
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